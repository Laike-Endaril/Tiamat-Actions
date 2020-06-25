package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUIGradient;
import com.fantasticsource.mctools.gui.element.other.GUILine;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.view.GUIPanZoomView;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.datastructures.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class EventEditorGUI extends GUIScreen
{
    public static final Color[]
            GREEN = new Color[]{getIdleColor(Color.GREEN), getHoverColor(Color.GREEN), Color.GREEN},
            WHITE = new Color[]{getIdleColor(Color.WHITE), getHoverColor(Color.WHITE), Color.WHITE},
            RED = new Color[]{getIdleColor(Color.RED), getHoverColor(Color.RED), Color.RED};

    protected CAction action;
    protected String event;
    protected GUIPanZoomView view;

    public EventEditorGUI(CAction action, String event)
    {
        show(action, event);
    }

    protected void show(CAction action, String event)
    {
        show();


        this.action = action;
        this.event = event;


        //Background
        root.add(new GUIGradient(this, 0, 0, 1, 1, Color.BLACK));


        //Header
        GUINavbar navbar = new GUINavbar(this);
        root.add(navbar);


        //Node view
        view = new GUIPanZoomView(this, 1, 1 - navbar.height);
        root.add(view);
        double wConversion = 1d / view.absolutePxWidth(), hConversion = 1d / view.absolutePxHeight();
        int scale = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        for (CNode node : action.EVENT_NODES.get(event).values())
        {
            view.add(new GUINode(this, (node.x - GUINode.FULL_SIZE * 0.5 * scale) * wConversion, (node.y - GUINode.FULL_SIZE * 0.5 * scale) * hConversion, node));
        }
        refreshNodeConnections();


        //GUI Actions
        navbar.addRecalcActions(() -> view.height = 1 - navbar.height);
        recalc();
    }


    public void refreshNodeConnections()
    {
        for (GUIElement element : view.children.toArray(new GUIElement[0]))
        {
            if (element instanceof GUILine) view.remove(element);
        }

        double wConversion = 1d / view.absolutePxWidth(), hConversion = 1d / view.absolutePxHeight();
        for (CNode node : action.EVENT_NODES.get(event).values())
        {
            double nodeX = node.x * wConversion, nodeY = node.y * hConversion;
            for (long position : node.inputNodePositions)
            {
                CNode inputNode = action.EVENT_NODES.get(node.eventName).get(position);
                double inputNodeX = inputNode.x * wConversion, inputNodeY = inputNode.y * hConversion;

                GUILine guiLine = new GUILine(this, inputNodeX, inputNodeY, nodeX, nodeY, EventEditorGUI.GREEN[0], EventEditorGUI.GREEN[1], EventEditorGUI.GREEN[2]);
                GUILine guiLine2 = new GUILine(this, inputNodeX, inputNodeY, (inputNodeX + nodeX) * 0.5, (inputNodeY + nodeY) * 0.5, EventEditorGUI.GREEN[0], EventEditorGUI.GREEN[1], EventEditorGUI.GREEN[2], 3);

                view.add(0, guiLine);
                view.add(0, guiLine2);
            }
        }
    }


    @Override
    public String title()
    {
        return event;
    }
}
