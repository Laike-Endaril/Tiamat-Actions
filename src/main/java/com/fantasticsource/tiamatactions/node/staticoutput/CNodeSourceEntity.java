package com.fantasticsource.tiamatactions.node.staticoutput;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.node.CNode;
import net.minecraft.entity.Entity;

public class CNodeSourceEntity extends CNode
{
    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeSourceEntity()
    {
        super();
    }

    public CNodeSourceEntity(String actionName, String event, int x, int y)
    {
        super(actionName, event, x, y);
    }


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
        return CAction.ALL_ACTIONS.get(actionName).source;
    }
}
