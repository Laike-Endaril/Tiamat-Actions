package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeSubAction extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/sub_action.png");
    protected static final Pair<String, Class> OPTIONAL_INPUTS = new Pair<>("forEach", Object.class);
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("actionName", Object.class);
    }

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
        return Object.class;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        CAction newSubAction = CAction.ALL_ACTIONS.get(inputs[0]);
        if (newSubAction == null || newSubAction.tickEndpointNodes.size() > 0) throw new IllegalArgumentException("Cannot run actions with tick tasks as sub-actions!");

        if (inputs.length == 1) return newSubAction.queue(mainAction.source, null, mainAction.mainAction);

        if (inputs.length == 2) return newSubAction.queue(mainAction.source, null, mainAction.mainAction, inputs[1]);

        for (int i = 1; i < inputs.length; i++)
        {
            newSubAction.queue(mainAction.source, null, mainAction.mainAction, inputs[i]);
        }
        return null;
    }
}
