package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.mctools.gui.element.view.GUIPanZoomView;
import com.fantasticsource.mctools.gui.screen.TextSelectionGUI;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tiamatactions.node.staticoutput.CNodeString;
import com.fantasticsource.tools.Tools;

public class GUINodeView extends GUIPanZoomView
{
    public GUINodeView(GUIScreen screen, double width, double height, GUIElement... subElements)
    {
        super(screen, width, height, subElements);
    }

    public GUINodeView(GUIScreen screen, double x, double y, double width, double height, GUIElement... subElements)
    {
        super(screen, x, y, width, height, subElements);
    }

    @Override
    public void click()
    {
        boolean subElementClicked = false;
        for (GUIElement element : children)
        {
            if (element.isMouseWithin())
            {
                subElementClicked = true;
                break;
            }
        }

        if (!subElementClicked)
        {
            int xx = (int) (viewPxX() + (mouseX() - absoluteX()) / absoluteWidth() * viewPxW()), yy = (int) (viewPxY() + (mouseY() - absoluteY()) / absoluteHeight() * viewPxH());
            if (!wellSpaced(xx - GUINode.FULL_SIZE, yy - GUINode.FULL_SIZE))
            {
                System.out.println("Test");
                //TODO
            }
            else
            {
                GUIText textElement = new GUIText(screen, "String");
                new TextSelectionGUI(textElement, "Select Node Type...", "String", "Number", "Source Entity", "", "Command", "Debug", "End Action", "Evaluate", "Get Action Variable", "Set Action Variable", "Run Sub-Action").addOnClosedActions(() ->
                {
                    EventEditorGUI gui = (EventEditorGUI) screen;
                    CAction action = gui.action;
                    CNode node = null;

                    switch (textElement.getText())
                    {
                        case "String":
                            node = new CNodeString(action.name, gui.event, xx, yy);
                            break;
                    }

                    if (node != null)
                    {
                        action.EVENT_NODES.get(gui.event).put(Tools.getLong(node.x, node.y), node);
                        action.EVENT_ENDPOINT_NODES.get(gui.event).add(node);

                        double wConversion = 1d / absolutePxWidth(), hConversion = 1d / absolutePxHeight();
                        add(new GUINode(screen, (node.x - GUINode.FULL_SIZE) * wConversion, (node.y - GUINode.FULL_SIZE) * hConversion, node));
                    }
                });
            }
        }

        super.click();
    }

    protected boolean wellSpaced(int xx, int yy)
    {
        int ww = absolutePxWidth(), hh = absolutePxHeight();
        for (GUIElement element : children)
        {
            if (!(element instanceof GUINode)) continue;

            if (Tools.distanceSquared(element.x * ww, element.y * hh, xx, yy) < GUINode.MIN_DISTANCE_SQUARED)
            {
                return false;
            }
        }

        return true;
    }
}
