package com.fantasticsource.tiamatactions.action;

import com.fantasticsource.tiamatactions.task.Task;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.Stack;

public class Action
{
    protected ArrayList<Task> onRunTasks = new ArrayList<>(), onEnqueueTasks = new ArrayList<>();
    protected Stack<Task> onEndTasks = new Stack<>();
    protected String[] tags;

    protected Action(String name, String... tags)
    {
        Actions.allActions.put(name, this);
        this.tags = tags;
    }

    public boolean enqueue(ICommandSender controller)
    {
        for (Task task : onEnqueueTasks)
        {
            if (task.getStopOnFailure() && !task.run(controller)) return false;
        }

        //TODO if no action is currently queued, run immediate
        //TODO else, queue this action
        return true;
    }

    protected boolean run(ICommandSender controller)
    {
        boolean result = true;

        //TODO Set current action to this action

        for (Task task : onRunTasks)
        {
            if (task.getStopOnFailure() && !task.run(controller))
            {
                result = false;
                break;
            }
        }

        for (Task task : onEndTasks)
        {
            task.run(controller);
        }

        //TODO Set current action to null

        return result;
    }
}
