package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import net.minecraft.util.ResourceLocation;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeEndAction extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/end_action.png");

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeEndAction()
    {
        super();
    }

    public CNodeEndAction(String actionName, String event, int x, int y)
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
    public Class outputType()
    {
        return null;
    }


    @Override
    public Object execute(CAction parentAction, Object... inputs)
    {
        parentAction.active = false;

        return null;
    }
}
