package com.fantasticsource.tiamatactions.action;

import com.fantasticsource.tiamatactions.task.CTask;
import com.fantasticsource.tools.Tools;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Action
{
    public static LinkedHashMap<String, Action> allActions = new LinkedHashMap<>();

    public final String NAME;
    protected String[] tags;
    public final ArrayList<CTask> tasks = new ArrayList<>();

    protected Action(String name, String... tags)
    {
        NAME = name;
        allActions.put(name, this);
        this.tags = tags;
    }


    public static Action getInstance(String name)
    {
        if (name == null || name.equals("") || name.equals("New Action")) throw new IllegalArgumentException("Action name must not be null, empty, or default (New Action)!");

        return new Action(name);
    }


    public CTask getTaskChain(int index)
    {
        return getTaskChain(index, Integer.MAX_VALUE);
    }

    public CTask getTaskChain(int index, int maxCount)
    {
        CTask task = null, nextTask;

        for (int i = Tools.min(tasks.size() - 1, index + maxCount - 1); i >= index && i >= 0; i--)
        {
            nextTask = task;
            task = (CTask) tasks.get(i).copy();
            if (nextTask != null) task.nextTasks.add(nextTask);
        }

        return task;
    }
}
