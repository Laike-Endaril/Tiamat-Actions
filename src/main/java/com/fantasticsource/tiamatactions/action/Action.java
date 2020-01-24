package com.fantasticsource.tiamatactions.action;

import com.fantasticsource.tiamatactions.task.Task;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.Stack;

public class Action
{
    protected ArrayList<Task> tasks = new ArrayList<>();
    protected Stack<Task> onCancelTasks = new Stack<>();

    protected Action(String name)
    {
        Actions.actions.put(name, this);
    }

    public void run(ICommandSender controller, Object... args)
    {
        for (Task task : tasks)
        {
            if (task.getStopOnFailure() && !task.run(controller, args)) break;
        }
    }
}
