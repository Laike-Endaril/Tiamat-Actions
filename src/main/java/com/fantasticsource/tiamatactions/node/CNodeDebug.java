package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import net.minecraft.util.text.TextComponentString;

public class CNodeDebug extends CNode
{
    @Override
    public String getDescription()
    {
        return "Display one or more values";
    }

    @Override
    public Class[] requiredInputTypes()
    {
        return new Class[0];
    }

    @Override
    public Class arrayInputType()
    {
        return Object.class;
    }

    @Override
    public Class[] outputTypes()
    {
        return new Class[]{};
    }

    @Override
    public Object[] execute(CAction parentAction, Object... inputs)
    {
        for (Object input : inputs) parentAction.source.sendMessage(new TextComponentString(input.toString()));

        return new Object[0];
    }
}
