package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeGetEntityGravity extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/get_entity_gravity.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("entity", Entity.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeGetEntityGravity()
    {
        super();
    }

    public CNodeGetEntityGravity(String actionName, String event, int x, int y)
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
        return "Output whether an entity uses gravity";
    }


    @Override
    public LinkedHashMap<String, Class> getRequiredInputs()
    {
        return REQUIRED_INPUTS;
    }

    @Override
    public Pair<String, Class> getOptionalInputs()
    {
        return null;
    }

    @Override
    public Class outputType()
    {
        return Boolean.class;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        Entity entity = (Entity) inputs[0];
        return !entity.hasNoGravity();
    }
}
