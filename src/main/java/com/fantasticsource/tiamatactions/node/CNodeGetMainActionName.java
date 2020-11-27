package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeGetMainActionName extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/get_main_action_name.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();
    protected static final Pair<String, Class> OPTIONAL_INPUTS = new Pair<>("action", CAction.class);

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeGetMainActionName()
    {
        super();
    }

    public CNodeGetMainActionName(String actionName, String event, int x, int y)
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
        return "Output the name of an action's main action (the action that appears in an action queue, ie. not a subaction name)";
    }


    @Override
    public LinkedHashMap<String, Class> getRequiredInputs()
    {
        return REQUIRED_INPUTS;
    }

    @Override
    public Pair<String, Class> getOptionalInputs()
    {
        return OPTIONAL_INPUTS;
    }

    @Override
    public Class outputType()
    {
        return String.class;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        return inputs.length == 0 ? mainAction.name : ((CAction) inputs[0]).mainAction.name;
    }
}
