package com.fantasticsource.tiamatactions.task;

import com.fantasticsource.tiamatactions.action.Action;
import com.fantasticsource.tiamatactions.action.ActionTaskHandler;
import com.fantasticsource.tiamatactions.gui.TaskGUI;

public class TaskDelay extends Task
{
    protected int delay = 20, elapsed = 0;


    public TaskDelay(Action action)
    {
        super(action);
    }


    @Override
    public String getDescription()
    {
        return "Delay for " + delay + " ticks";
    }

    @Override
    public void tick(ActionTaskHandler handler)
    {
        if (elapsed++ == delay) handler.currentTasks.addAll(nextTasks);
        else handler.queuedTasks.add(this);
    }

    @Override
    public TaskGUI getTaskGUI()
    {
        //TODO
        return null;
    }
}
