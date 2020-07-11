package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.TrigLookupTable;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeSetLookVector extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/set_look_vector.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("entity", Entity.class);
        REQUIRED_INPUTS.put("lookVec", Vec3d.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeSetLookVector()
    {
        super();
    }

    public CNodeSetLookVector(String actionName, String event, int x, int y)
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
        return "Set an entity's look vector";
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
        Vec3d vector = (Vec3d) inputs[1];

        Vec3d pos1 = entity.getPositionVector().add(new Vec3d(0, entity.getEyeHeight(), 0));
        Vec3d pos2 = pos1.add(vector);

        float yaw = (float) MCTools.getYawDeg(pos1, pos2, TrigLookupTable.TRIG_TABLE_1024);
        float pitch = (float) MCTools.getPitchDeg(pos1, pos2, TrigLookupTable.TRIG_TABLE_1024);

        entity.setRotationYawHead(yaw);

        if (entity instanceof EntityPlayerMP) ((EntityPlayerMP) entity).connection.setPlayerLocation(entity.posX, entity.posY, entity.posZ, yaw, pitch);
        else
        {
            entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, yaw, pitch);
            entity.world.updateEntityWithOptionalForce(entity, false);
        }

        return null;
    }
}
