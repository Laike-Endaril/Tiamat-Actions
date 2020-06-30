package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.text.GUIFadingText;
import com.fantasticsource.mctools.gui.element.textured.GUIImage;
import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.Collision;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.TrigLookupTable;
import com.fantasticsource.tools.datastructures.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.input.Keyboard;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;

public class GUINode extends GUIImage
{
    protected static final TrigLookupTable SIDE_STEPPER = TrigLookupTable.getInstance(64);
    public static final int ICON_SIZE = 32, ERROR_BORDER_THICKNESS = 4, FULL_SIZE = ICON_SIZE + (ERROR_BORDER_THICKNESS << 1), HALF_SIZE = FULL_SIZE >>> 1, MIN_DISTANCE_SQUARED = (FULL_SIZE << 2) * (FULL_SIZE << 2);
    public static final double ERROR_BORDER_PERCENT = (double) ERROR_BORDER_THICKNESS / FULL_SIZE;
    protected static double mouseAnchorX, mouseAnchorY;
    protected static boolean createEditDragging = false;

    protected CNode node;

    public GUINode(GUIScreen screen, double x, double y, CNode node)
    {
        super(screen, x, y, FULL_SIZE, FULL_SIZE, node.getTexture());
        ignoreMCGUIScale(true);
        this.node = node;
        updateTooltip();
    }


    @Override
    protected void tick()
    {
        updateTooltip();
    }


    protected void updateTooltip()
    {
        GUINodeView view = (GUINodeView) parent;
        if (view == null) return;

        if (this == view.tempNode) setTooltip(null);
        else
        {
            String error = node.error(((EventEditorGUI) screen).action);
            setTooltip(error == null ? node.getDescription() : node.getDescription() + " (" + error + ")");
        }
    }


    @Override
    public boolean mousePressed(int button)
    {
        boolean result = super.mousePressed(button);

        if (isMouseWithin())
        {
            if (button == TiamatActionsConfig.clientSettings.guiSettings.createEditNodeButton)
            {
                GUINodeView view = (GUINodeView) parent;
                mouseAnchorX = (mouseX() - absoluteX()) / absoluteWidth();
                mouseAnchorY = (mouseY() - absoluteY()) / absoluteHeight();
                view.tempNode = new GUINode(screen, x, y, node);
                view.add(view.tempNode);

                result = true;
            }
            else if (button == TiamatActionsConfig.clientSettings.guiSettings.createNodeConnectionButton)
            {
                GUINodeView view = (GUINodeView) parent;

                if (view.longConnector == null)
                {
                    view.longConnector = new GUITempConnector(screen, (GUINodeView) parent, node, false);
                    view.shortConnector = new GUITempConnector(screen, (GUINodeView) parent, node, true);

                    parent.add(0, view.longConnector);
                    parent.add(0, view.shortConnector);
                }
                else
                {
                    if (node != view.longConnector.from)
                    {
                        String error = node.tryAddInput(((EventEditorGUI) screen).action, view.longConnector.from);

                        if (error == null)
                        {
                            ((EventEditorGUI) screen).refreshNodeConnections();
                        }
                        else
                        {
                            parent.parent.add(new GUIFadingText(screen, parent.x + 5d / screen.pxWidth, parent.y + 5d / screen.pxHeight, "Cannot add connection; " + error, 150, 300, Color.RED));
                        }
                    }


                    view.remove(view.longConnector);
                    view.remove(view.shortConnector);

                    view.longConnector = null;
                    view.shortConnector = null;
                }

                result = true;
            }
        }

        return result;
    }


    @Override
    public void mouseDrag(int button)
    {
        GUINodeView view = (GUINodeView) parent;
        if (button == TiamatActionsConfig.clientSettings.guiSettings.createEditNodeButton && view.tempNode != null && view.tempNode.node == node)
        {
            createEditDragging = true;

            view.tempNode.setAbsoluteX(mouseX() - mouseAnchorX * absoluteWidth());
            view.tempNode.setAbsoluteY(mouseY() - mouseAnchorY * absoluteHeight());

            Color color = view.tempNode.wellSpaced() ? Color.WHITE : Color.RED;
            if (view.tempNode.color != color) view.tempNode.setColor(color);
        }

        super.mouseDrag(button);
    }

    @Override
    public boolean mouseReleased(int button)
    {
        boolean result = super.mouseReleased(button);
        if (parent == null) return result;


        GUINodeView view = (GUINodeView) parent;

        if (button == TiamatActionsConfig.clientSettings.guiSettings.createEditNodeButton && view.tempNode != null && view.tempNode.node == node)
        {
            if (createEditDragging)
            {
                boolean wellSpaced = view.tempNode.wellSpaced();

                view.tempNode.parent.remove(view.tempNode);

                if (wellSpaced)
                {
                    x = view.tempNode.x;
                    y = view.tempNode.y;

                    int xx = (int) (x * parent.absolutePxWidth() + GUINode.HALF_SIZE), yy = (int) (y * parent.absolutePxHeight() + GUINode.HALF_SIZE);
                    node.setPosition(((EventEditorGUI) screen).action, xx, yy, this);
                }
                else
                {
                    view.parent.add(new GUIFadingText(screen, view.x + 5d / screen.pxWidth, view.y + 5d / screen.pxHeight, "Cannot move node here: too close to another node", 150, 300, Color.RED));
                }

                view.tempNode = null;
                createEditDragging = false;
            }
            else
            {
                view.tempNode.parent.remove(view.tempNode);
                view.tempNode = null;

                GUIScreen gui = node.showNodeEditGUI();
                if (gui != null) gui.addOnClosedActions(this::updateTooltip);
            }

            result = true;
        }
        else if (button == TiamatActionsConfig.clientSettings.guiSettings.createNodeConnectionButton && isMouseWithin() && view.longConnector != null && node != view.longConnector.from)
        {
            String error = node.tryAddInput(((EventEditorGUI) screen).action, view.longConnector.from);

            if (error == null)
            {
                ((EventEditorGUI) screen).refreshNodeConnections();
            }
            else
            {
                parent.parent.add(new GUIFadingText(screen, parent.x + 5d / screen.pxWidth, parent.y + 5d / screen.pxHeight, "Cannot add connection; " + error, 150, 300, Color.RED));
            }


            view.remove(view.longConnector);
            view.remove(view.shortConnector);

            view.longConnector = null;
            view.shortConnector = null;
        }

        return result;
    }

    protected boolean wellSpaced()
    {
        GUINodeView view = (GUINodeView) parent;

        int ww = view.tempNode.parent.absolutePxWidth(), hh = view.tempNode.parent.absolutePxHeight();
        double xx = view.tempNode.x * ww, yy = view.tempNode.y * hh;
        for (GUIElement element : parent.children)
        {
            if (!(element instanceof GUINode)) continue;
            if (element == this || ((GUINode) element).node == node) continue;

            if (Tools.distanceSquared(element.x * ww, element.y * hh, xx, yy) < MIN_DISTANCE_SQUARED)
            {
                return false;
            }
        }

        return true;
    }


    @Override
    public void keyTyped(char typedChar, int keyCode)
    {
        if (keyCode == Keyboard.KEY_DELETE && isMouseWithin())
        {
            EventEditorGUI gui = (EventEditorGUI) screen;

            for (GUIElement element : parent.children.toArray(new GUIElement[0]))
            {
                if (element instanceof GUIConnector) parent.remove(element);
            }


            parent.remove(this);
            node.delete(((EventEditorGUI) screen).action);


            gui.refreshNodeConnections();
        }

        super.keyTyped(typedChar, keyCode);
    }


    @Override
    public boolean isWithin(double x, double y)
    {
        return Collision.pointCircle(x * screen.pxWidth, y * screen.pxHeight, absolutePxX() + absolutePxWidth() * 0.5, absolutePxY() + absolutePxHeight() * 0.5, absolutePxWidth() * 0.5 * ICON_SIZE / FULL_SIZE);
    }

    @Override
    public void draw()
    {
        GUINodeView view = (GUINodeView) parent;

        if (node.error(((EventEditorGUI) screen).action) != null)
        {
            GlStateManager.disableTexture2D();
            GlStateManager.glBegin(GL_TRIANGLE_FAN);
            if (this == view.tempNode) GlStateManager.color(1, 0, 0, 0.5f);
            else if (view.tempNode != null && node == view.tempNode.node) GlStateManager.color(1, 0, 0, 0.3f);
            else GlStateManager.color(1, 0, 0, 1);
            double cos, sin;
            double[] array = SIDE_STEPPER.getArray();
            for (int i = 0; i < array.length; i++)
            {
                sin = array[i];
                cos = array[Tools.posMod(i + (array.length >> 2), array.length)];
                GlStateManager.glVertex3f(0.5f + (float) (cos * 0.5), 0.5f - (float) (sin * 0.5), 0);
            }
            GlStateManager.glEnd();
        }


        GlStateManager.enableTexture2D();
        if (this == view.tempNode) GlStateManager.color(color.rf(), color.gf(), color.bf(), color.af() * 0.5f);
        else if (view.tempNode != null && node == view.tempNode.node) GlStateManager.color(color.rf(), color.gf(), color.bf(), color.af() * 0.3f);
        else GlStateManager.color(color.rf(), color.gf(), color.bf(), color.af());

        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

        double u2 = u + uw, v2 = v + vh;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();

        builder.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.pos(1 - ERROR_BORDER_PERCENT, ERROR_BORDER_PERCENT, 0).tex(u2, v).endVertex();
        builder.pos(ERROR_BORDER_PERCENT, ERROR_BORDER_PERCENT, 0).tex(u, v).endVertex();
        builder.pos(ERROR_BORDER_PERCENT, 1 - ERROR_BORDER_PERCENT, 0).tex(u, v2).endVertex();
        builder.pos(1 - ERROR_BORDER_PERCENT, 1 - ERROR_BORDER_PERCENT, 0).tex(u2, v2).endVertex();
        tessellator.draw();

        GlStateManager.color(1, 1, 1, 1);


        drawChildren();
    }
}
