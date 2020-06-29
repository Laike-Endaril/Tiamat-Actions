package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import net.minecraft.util.ResourceLocation;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeSubAction extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/sub_action.png");

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeSubAction()
    {
        super();
    }

    public CNodeSubAction(String actionName, String event, int x, int y)
    {
        super(actionName, event, x, y);
    }


    @Override
    public ResourceLocation getTexture()
    {
        return TEXTURE;
    }

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
    public Object execute(CAction mainAction, Object... inputs)
    {
        CAction subAction = CAction.ALL_ACTIONS.get(inputs[0]);
        if (subAction == null || subAction.tickEndpointNodes.size() > 0) throw new IllegalArgumentException("Cannot run actions with tick tasks as sub-actions!");

        subAction.queue(mainAction.source, mainAction.queue.name, mainAction.mainAction);

        return null;
    }
}
