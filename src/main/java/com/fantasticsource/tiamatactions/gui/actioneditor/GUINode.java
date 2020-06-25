package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.textured.GUIImage;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.TrigLookupTable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;

public class GUINode extends GUIImage
{
    protected static final TrigLookupTable SIDE_STEPPER = TrigLookupTable.getInstance(64);
    public static final int ICON_SIZE = 32, ERROR_BORDER_THICKNESS = 4, FULL_SIZE = ICON_SIZE + (ERROR_BORDER_THICKNESS << 1);
    public static final double ERROR_BORDER_PERCENT = (double) ERROR_BORDER_THICKNESS / FULL_SIZE;
    protected static double mouseAnchorX, mouseAnchorY;
    protected static GUINode tempNode = null;

    protected CNode node;

    public GUINode(GUIScreen screen, double x, double y, CNode node)
    {
        super(screen, x, y, FULL_SIZE, FULL_SIZE, node.getTexture());
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
        if (this == tempNode) setTooltip(null);
        else
        {
            String error = node.error();
            setTooltip(error == null ? node.getDescription() : node.getDescription() + " (" + error + ")");
        }
    }


    @Override
    public void click()
    {
        node.showNodeEditGUI().addOnClosedActions(this::updateTooltip);

        super.click();
    }

    @Override
    public boolean mousePressed(int button)
    {
        if (button == 0 && isMouseWithin())
        {
            mouseAnchorX = (mouseX() - absoluteX()) / absoluteWidth();
            mouseAnchorY = (mouseY() - absoluteY()) / absoluteHeight();
            tempNode = new GUINode(screen, x, y, node);
            parent.add(tempNode);
        }

        return super.mousePressed(button);
    }

    @Override
    public void mouseDrag(int button)
    {
        if (button == 0 && active)
        {
            tempNode.setAbsoluteX(mouseX() - mouseAnchorX * absoluteWidth());
            tempNode.setAbsoluteY(mouseY() - mouseAnchorY * absoluteHeight());
        }

        super.mouseDrag(button);
    }

    @Override
    public boolean mouseReleased(int button)
    {
        if (button == 0 && active)
        {
            tempNode.parent.remove(tempNode);
            tempNode = null;
        }

        return super.mouseReleased(button);
    }

    @Override
    public void draw()
    {
        if (node.error() != null)
        {
            GlStateManager.disableTexture2D();
            GlStateManager.glBegin(GL_TRIANGLE_FAN);
            if (this == tempNode) GlStateManager.color(1, 0, 0, 0.5f);
            else if (tempNode != null && node == tempNode.node) GlStateManager.color(1, 0, 0, 0.3f);
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
        if (this == tempNode) GlStateManager.color(color.rf(), color.gf(), color.bf(), color.af() * 0.5f);
        else if (tempNode != null && node == tempNode.node) GlStateManager.color(color.rf(), color.gf(), color.bf(), color.af() * 0.3f);
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
