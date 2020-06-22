package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import net.minecraft.util.ResourceLocation;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeSetActionVar extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/setActionVar.png");

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeSetActionVar()
    {
        super();
    }

    public CNodeSetActionVar(String actionName, String event, int x, int y)
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
    public Class outputType()
    {
        return null;
    }


    @Override
    public Object execute(CAction parentAction, Object... inputs)
    {
        parentAction.actionVars.put((String) inputs[0], inputs[1]);

        return null;
    }
}
