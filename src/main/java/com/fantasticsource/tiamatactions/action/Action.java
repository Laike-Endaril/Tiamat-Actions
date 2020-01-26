package com.fantasticsource.tiamatactions.action;

import com.fantasticsource.tiamatactions.task.Task;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Stack;

public class Action
{
    protected ArrayList<Task> onRunTasks = new ArrayList<>(), onEnqueueTasks = new ArrayList<>();
    protected Stack<Task> onEndTasks = new Stack<>();
    protected String[] tags;
    protected ItemStack activatingItem = null;

    protected Action(String name, String... tags)
    {
        Actions.allActions.put(name, this);
        this.tags = tags;
    }

    public boolean enqueue(ICommandSender controller)
    {
        LinkedHashMap<String, Object> vars = new LinkedHashMap<>();
        for (Task task : onEnqueueTasks)
        {
            if (task.getStopOnFailure() && !task.run(controller, vars)) return false;
        }

        //TODO if no action is currently queued, run immediate
        //TODO else, queue this action
        return true;
    }

    protected boolean run(ICommandSender controller, @Nullable ItemStack activatingItem, LinkedHashMap<String, Object> vars)
    {
        boolean result = true;

        //TODO Set current action to this action

        for (Task task : onRunTasks)
        {
            if (task.getStopOnFailure() && !task.run(controller, vars))
            {
                result = false;
                break;
            }
        }

        for (Task task : onEndTasks)
        {
            task.run(controller, vars);
        }

        //TODO Set current action to null

        return result;
    }

    public ItemStack getActivatingItem()
    {
        return activatingItem;
    }
}
