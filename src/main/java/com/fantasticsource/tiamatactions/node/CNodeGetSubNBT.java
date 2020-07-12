package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.NotImplementedException;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeGetSubNBT extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/get_sub_nbt.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("nbt", NBTBase.class);
        REQUIRED_INPUTS.put("reference", String.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeGetSubNBT()
    {
        super();
    }

    public CNodeGetSubNBT(String actionName, String event, int x, int y)
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
        return "Get NBT from within other NBT";
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
        return NBTBase.class;
    }


    @Override
    public Object execute(CAction mainAction, Object... inputs)
    {
        NBTBase nbt = (NBTBase) inputs[0];
        for (String ref : Tools.fixedSplit("" + inputs[1], ":"))
        {
            if (nbt instanceof NBTTagCompound) nbt = ((NBTTagCompound) nbt).getTag(ref);
            else throw new NotImplementedException("Have not yet added sub-accessors for " + nbt.getClass().getSimpleName());
        }
        return nbt;
    }
}
