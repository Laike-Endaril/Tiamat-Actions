package com.fantasticsource.tiamatactions.gui;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.tiamatactions.task.Task;
import com.fantasticsource.tools.datastructures.Color;

public class GUITask extends GUIText
{
    protected Task task;

    public GUITask(GUIScreen screen, Task task)
    {
        this(screen, task, Color.WHITE);
    }

    public GUITask(GUIScreen screen, Task task, Color color)
    {
        super(screen, task.getDescription(), GUIScreen.getIdleColor(color), GUIScreen.getHoverColor(color), color);
        this.task = task;
    }


    public GUITask(GUIScreen screen, double x, double y, Task task)
    {
        this(screen, x, y, task, Color.WHITE);
    }

    public GUITask(GUIScreen screen, double x, double y, Task task, Color color)
    {
        super(screen, x, y, task.getDescription(), GUIScreen.getIdleColor(color), GUIScreen.getHoverColor(color), color);
        this.task = task;
    }


    @Override
    public void click()
    {
        task.getTaskGUI().show();

        super.click();
    }
}
