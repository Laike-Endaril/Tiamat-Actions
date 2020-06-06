package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import net.minecraft.entity.Entity;

public class CNodeGetSource extends CNode
{
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
    public Class outputType()
    {
        return Entity.class;
    }


    @Override
    public Object execute(CAction parentAction, Object... inputs)
    {
        return action.source;
    }
}
