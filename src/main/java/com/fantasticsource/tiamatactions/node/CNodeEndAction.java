package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;

public class CNodeEndAction extends CNode
{
    @Override
    public String getDescription()
    {
        return "End this action (and run action end tasks if the action has finished all initialization and start tasks)";
    }

    @Override
    public Class[] requiredInputTypes()
    {
        return new Class[0];
    }

    @Override
    public Class arrayInputType()
    {
        return null;
    }

    @Override
    public Class[] outputTypes()
    {
        return new Class[]{};
    }

    @Override
    public Object[] execute(CAction parentAction, Object... inputs)
    {
        parentAction.valid = false;

        return new Object[0];
    }
}
