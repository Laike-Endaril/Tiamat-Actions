package com.fantasticsource.tiamatactions.action;

import com.fantasticsource.tiamatactions.task.Task;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.Stack;

public class Action
{
    protected ArrayList<Task> onRunTasks = new ArrayList<>(), onEnqueueTasks = new ArrayList<>();
    protected Stack<Task> onCancelTasks = new Stack<>();

    protected Action(String name)
    {
        Actions.allActions.put(name, this);
    }

    public boolean enqueue(ICommandSender controller)
    {
        for (Task task : onEnqueueTasks)
        {
            if (task.getStopOnFailure() && !task.run(controller)) return false;
        }

        //TODO if no action is currently queued, run immediate
        //TODO else, queue action
        return true;
    }

    public boolean run(ICommandSender controller)
    {
        for (Task task : onRunTasks)
        {
            if (task.getStopOnFailure() && !task.run(controller)) return false;
        }

        return true;
    }
}
