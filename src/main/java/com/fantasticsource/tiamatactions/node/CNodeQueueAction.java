package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeQueueAction extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/queue_action.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();
    protected static final Pair<String, Class> OPTIONAL_INPUTS = new Pair<>("argument", Object.class);

    static
    {
        REQUIRED_INPUTS.put("entity", Entity.class);
        REQUIRED_INPUTS.put("queueName", Object.class);
        REQUIRED_INPUTS.put("actionName", Object.class);
        REQUIRED_INPUTS.put("inheritActionVars", Boolean.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeQueueAction()
    {
        super();
    }

    public CNodeQueueAction(String actionName, String event, int x, int y)
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
        return "Queue an action";
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
        return null;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        CAction action = CAction.ALL_ACTIONS.get("" + inputs[2]);
        Object argument = inputs.length > 4 ? inputs[4] : null;
        action.queue((Entity) inputs[0], "" + inputs[1], null, argument, (boolean) inputs[3] ? mainAction.actionVars : null);
        return null;
    }
}
