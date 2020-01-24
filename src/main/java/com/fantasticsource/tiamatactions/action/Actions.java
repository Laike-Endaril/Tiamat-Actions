package com.fantasticsource.tiamatactions.action;

import java.util.LinkedHashMap;

public class Actions
{
    public static LinkedHashMap<String, Action> allActions = new LinkedHashMap<>();

    public static Action getAction(String name)
    {
        if (name == null || name.equals("") || name.equals("New Action")) throw new IllegalArgumentException("Action name must not be null, empty, or default (New Action)!");

        return new Action(name);
    }
}
