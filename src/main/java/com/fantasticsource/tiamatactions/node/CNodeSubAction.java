package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;

public class CNodeSubAction extends CNode
{
    @Override
    public String getDescription()
    {
        return "Run another action as part of this action";
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
        return null;
    }


    @Override
    public Object execute(CAction parentAction, Object... inputs)
    {
        CAction subAction = CAction.ALL_ACTIONS.get(inputs[0]);
        if (subAction == null || subAction.tickEndpointNodes.size() > 0) throw new IllegalArgumentException("Cannot run actions with tick tasks as sub-actions!");

        subAction.queue(parentAction.source, parentAction.queue.name, parentAction.mainAction);

        return null;
    }
}
