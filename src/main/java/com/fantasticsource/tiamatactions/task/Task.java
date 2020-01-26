package com.fantasticsource.tiamatactions.task;

import com.fantasticsource.tiamatactions.action.ActionTaskHandler;
import com.fantasticsource.tiamatactions.action.Action;
import com.fantasticsource.tiamatactions.gui.TaskGUI;

import java.util.ArrayList;

public abstract class Task
{
    public final Action action;
    public ArrayList<Task> nextTasks = new ArrayList<>();


    public Task(Action action)
    {
        this.action = action;
    }


    public abstract String getDescription();


    public abstract void tick(ActionTaskHandler handler);


    public abstract TaskGUI getTaskGUI();
}
