package com.fantasticsource.tiamatactions.action;

import com.fantasticsource.tiamatactions.task.CTask;

import java.util.ArrayList;

public class Action
{
    public final String NAME;
    protected String[] tags;
    public final ArrayList<CTask> tasks = new ArrayList<>();

    protected Action(String name, String... tags)
    {
        NAME = name;
        Actions.allActions.put(name, this);
        this.tags = tags;
    }
}
