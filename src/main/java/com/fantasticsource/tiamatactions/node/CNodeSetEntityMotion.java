package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeSetEntityMotion extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/set_entity_motion.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("entity", Entity.class);
        REQUIRED_INPUTS.put("motion", Vec3d.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeSetEntityMotion()
    {
        super();
    }

    public CNodeSetEntityMotion(String actionName, String event, int x, int y)
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
        return "Set an entity's motion";
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
        return null;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        Entity entity = (Entity) inputs[0];
        Vec3d motion = (Vec3d) inputs[1];
        entity.motionX = motion.x;
        entity.motionY = motion.y;
        entity.motionZ = motion.z;
        return null;
    }
}
