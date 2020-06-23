package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.other.GUIGradient;
import com.fantasticsource.mctools.gui.element.other.GUILine;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.view.GUIPanZoomView;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.datastructures.Color;

public class EventEditorGUI extends GUIScreen
{
    protected CAction action;
    protected String event;

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
        GUIPanZoomView view = new GUIPanZoomView(this, 1, 1 - navbar.height);
        root.add(view);
        double wConversion = 1d / view.absolutePxWidth(), hConversion = 1d / view.absolutePxHeight();
        for (CNode node : action.EVENT_NODES.get(event).values())
        {
            GUINode guiNode = new GUINode(this, node.x * wConversion, node.y * hConversion, node);
            for (long position : node.inputNodePositions)
            {
                CNode inputNode = action.EVENT_NODES.get(event).get(position);
                GUILine guiLine = new GUILine(this, (node.x + GUINode.SIZE) * wConversion, (node.y + GUINode.SIZE) * hConversion, (inputNode.x + GUINode.SIZE) * wConversion, (inputNode.y + GUINode.SIZE) * hConversion, Color.WHITE);
                view.add(0, guiLine);
            }
            view.add(guiNode);
        }


        //GUI Actions
        navbar.addRecalcActions(() -> view.height = 1 - navbar.height);
        recalc();
    }

    @Override
    public String title()
    {
        return event;
    }
}
