package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeSpawnEntityWithNBT extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/spawn_entity_with_nbt.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("entityRegistryName", Object.class);
        REQUIRED_INPUTS.put("world", World.class);
        REQUIRED_INPUTS.put("position", Vec3d.class);
        REQUIRED_INPUTS.put("nbt", NBTTagCompound.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeSpawnEntityWithNBT()
    {
        super();
    }

    public CNodeSpawnEntityWithNBT(String actionName, String event, int x, int y)
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
        return "Spawn an entity with specified NBT and return it";
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
        return Entity.class;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        World world = (World) inputs[1];
        Vec3d pos = (Vec3d) inputs[2];
        NBTTagCompound compound = (NBTTagCompound) inputs[3];

        compound.setString("id", "" + inputs[0]);
        Entity entity = AnvilChunkLoader.readWorldEntityPos(compound, world, pos.x, pos.y, pos.z, true);

        if (entity != null) entity.setLocationAndAngles(pos.x, pos.y, pos.z, entity.rotationYaw, entity.rotationPitch);

        return entity;
    }
}
