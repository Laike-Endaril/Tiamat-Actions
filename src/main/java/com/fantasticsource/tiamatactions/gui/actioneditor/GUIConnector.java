package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUILine;
import com.fantasticsource.tools.datastructures.Color;

public class GUIConnector extends GUILine
{
    public GUIConnector(GUIScreen screen, double x1, double y1, double x2, double y2, Color color)
    {
        super(screen, x1, y1, x2, y2, color);
    }

    public GUIConnector(GUIScreen screen, double x1, double y1, double x2, double y2, Color color, float thickness)
    {
        super(screen, x1, y1, x2, y2, color, thickness);
    }

    public GUIConnector(GUIScreen screen, double x1, double y1, double x2, double y2, Color color, Color hoverColor, Color activeColor)
    {
        super(screen, x1, y1, x2, y2, color, hoverColor, activeColor);
    }

    public GUIConnector(GUIScreen screen, double x1, double y1, double x2, double y2, Color color, Color hoverColor, Color activeColor, float thickness)
    {
        super(screen, x1, y1, x2, y2, color, hoverColor, activeColor, thickness);
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
