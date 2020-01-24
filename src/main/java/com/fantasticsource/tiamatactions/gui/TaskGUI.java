package com.fantasticsource.tiamatactions.gui;

import com.fantasticsource.mctools.gui.GUIScreen;

public abstract class TaskGUI extends GUIScreen
{
    @Override
    public String title()
    {
        return null;
    }

    public final void show()
    {
        root.clear();

        //TODO navbar
        //TODO tabview
        //TODO base task selection in 1st tab

        //TODO have the submethod add stuff to the 2nd tab
        addTaskElements();

        //TODO activate 2nd tab
    }

    protected abstract void addTaskElements();
}
