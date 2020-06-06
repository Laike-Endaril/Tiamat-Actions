package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;

public class CNodeGetActionVar extends CNode
{
    @Override
    public String getDescription()
    {
        return "Output the given action variable";
    }


    @Override
    public Class[] requiredInputTypes()
    {
        return new Class[]{String.class};
    }

    @Override
    public Class arrayInputType()
    {
        return null;
    }

    @Override
    public Class outputType()
    {
        return Object.class;
    }


    @Override
    public Object execute(CAction parentAction, Object... inputs)
    {
        return action.actionVars.get(inputs[0]);
    }
}
