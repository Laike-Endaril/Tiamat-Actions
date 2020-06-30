package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUILine;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.datastructures.Color;

public class GUITempConnector extends GUILine
{
    public static final Color[]
            YELLOW = new Color[]{GUIScreen.getIdleColor(Color.YELLOW).setAF(0.3f), GUIScreen.getHoverColor(Color.YELLOW).setAF(0.3f), Color.YELLOW.copy().setAF(0.3f)},
            RED = new Color[]{GUIScreen.getIdleColor(Color.RED).setAF(0.3f), GUIScreen.getHoverColor(Color.RED).setAF(0.3f), Color.RED.copy().setAF(0.3f)};

    protected boolean halfPart;
    protected CNode from;

    public GUITempConnector(GUIScreen screen, GUINodeView view, CNode from, boolean halfPart)
    {
        super(screen, (double) from.x / view.absolutePxWidth(), (double) from.y / view.absolutePxHeight(), (double) from.x / view.absolutePxWidth(), (double) from.y / view.absolutePxHeight(), RED[0], RED[1], RED[2], halfPart ? 3 : 1);
        useParentScissor(true);

        this.halfPart = halfPart;
        this.from = from;
    }


    @Override
    protected void tick()
    {
        GUINode hovered = null;

        color = RED[0];
        hoverColor = RED[1];
        activeColor = RED[2];
        for (GUIElement element : parent.children)
        {
            if (!(element instanceof GUINode)) continue;
            if (((GUINode) element).node == from) continue;

            if (element.isMouseWithin())
            {
                color = YELLOW[0];
                hoverColor = YELLOW[1];
                activeColor = YELLOW[2];


                hovered = (GUINode) element;
                x2 = (double) hovered.node.x / parent.absolutePxWidth();
                y2 = (double) hovered.node.y / parent.absolutePxHeight();


                break;
            }
        }


        if (hovered == null)
        {
            GUINodeView view = (GUINodeView) parent;
            x2 = (view.viewPxX() + (mouseX() - view.absoluteX()) / view.absoluteWidth() * view.viewPxW()) / view.absolutePxWidth();
            y2 = (view.viewPxY() + (mouseY() - view.absoluteY()) / view.absoluteHeight() * view.viewPxH()) / view.absolutePxHeight();
        }


        if (halfPart)
        {
            x2 = (x1 + x2) * 0.5;
            y2 = (y1 + y2) * 0.5;
        }

        isDownRight = x1 < x2 == y1 < y2;

        x = Tools.min(x1, x2);
        y = Tools.min(y1, y2);

        width = Math.abs(x2 - x1);
        height = Math.abs(y2 - y1);


        super.tick();
    }

    @Override
    public boolean mouseReleased(int button)
    {
        GUINodeView view = (GUINodeView) parent;

        if (!halfPart && button == 1 && isMouseWithin())
        {
            view.longConnector.parent.remove(view.longConnector);
            view.shortConnector.parent.remove(view.shortConnector);

            view.longConnector = null;
            view.shortConnector = null;
        }

        return super.mouseReleased(button);
    }

    @Override
    public boolean isWithin(double x, double y)
    {
        for (GUIElement element : parent.children)
        {
            if (element instanceof GUINode && element.isWithin(x, y)) return false;
        }

        return super.isWithin(x, y);
    }
}
