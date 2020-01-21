package com.fantasticsource.tiamatactions;

import com.fantasticsource.tiamatactions.task.Task;
import com.fantasticsource.tools.datastructures.ExplicitPriorityQueue;
import net.minecraft.command.ICommandSender;

import java.util.LinkedHashMap;

public class Action
{
    public static LinkedHashMap<String, Action> actions = new LinkedHashMap<>();

    protected ExplicitPriorityQueue<Task> tasks = new ExplicitPriorityQueue<>();

    protected Action(String name)
    {
        actions.put(name, this);
    }

    public static Action getInstance(String name)
    {
        if (name == null || name.equals("") || name.equals("New Action")) throw new IllegalArgumentException("Action name must not be null, empty, or default (New Action)!");

        return new Action(name);
    }

    public static void run(ICommandSender controller, Object... args)
    {
        //TODO run tasks
    }
}
