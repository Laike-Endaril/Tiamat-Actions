package com.fantasticsource.tiamatactions.node.vector;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeSetPosition extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/set_position.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("entity", EntityLivingBase.class);
        REQUIRED_INPUTS.put("position", Vec3d.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeSetPosition()
    {
        super();
    }

    public CNodeSetPosition(String actionName, String event, int x, int y)
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
        return "Set an entity's position";
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
    public Object execute(CAction mainAction, Object... inputs)
    {
        Entity entity = (Entity) inputs[0];
        Vec3d position = (Vec3d) inputs[1];
        entity.setPositionAndUpdate(position.x, position.y, position.z);
        return null;
    }
}
