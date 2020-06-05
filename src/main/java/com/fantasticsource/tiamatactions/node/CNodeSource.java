package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import net.minecraft.entity.Entity;

public class CNodeSource extends CNode
{
    public boolean toSubaction = false;

    @Override
    public String getDescription()
    {
        return "Output the entity which is taking this action";
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
        return new Class[]{Entity.class};
    }

    @Override
    public Object[] execute(CAction parentAction, Object... inputs)
    {
        return new Object[]{action.source};
    }
}
