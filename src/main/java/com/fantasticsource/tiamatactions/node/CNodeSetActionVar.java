package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;

public class CNodeSetActionVar extends CNode
{
    @Override
    public String getDescription()
    {
        return "Set an action variable to a value";
    }

    @Override
    public Class[] requiredInputTypes()
    {
        return new Class[]{String.class, Object.class};
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
        parentAction.actionVars.put((String) inputs[0], inputs[1]);

        return new Object[0];
    }
}
