package com.fantasticsource.tiamatactions.gui;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.datastructures.Color;

public class GUINode extends GUIText
{
    protected CNode task;

    public GUINode(GUIScreen screen, CNode task)
    {
        this(screen, task, Color.WHITE);
    }

    public GUINode(GUIScreen screen, CNode task, Color color)
    {
        super(screen, task.getDescription(), GUIScreen.getIdleColor(color), GUIScreen.getHoverColor(color), color);
        this.task = task;
    }


    public GUINode(GUIScreen screen, double x, double y, CNode task)
    {
        this(screen, x, y, task, Color.WHITE);
    }

    public GUINode(GUIScreen screen, double x, double y, CNode task, Color color)
    {
        super(screen, x, y, task.getDescription(), GUIScreen.getIdleColor(color), GUIScreen.getHoverColor(color), color);
        this.task = task;
    }


    @Override
    public void click()
    {
//        task.getTaskGUI().show();

        super.click();
    }
}
