package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.NotImplementedException;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeGetNBTValue extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/get_nbt_value.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("nbt", NBTBase.class);
        REQUIRED_INPUTS.put("reference", String.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeGetNBTValue()
    {
        super();
    }

    public CNodeGetNBTValue(String actionName, String event, int x, int y)
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
        return "Get a NBT tag value";
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
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        NBTBase nbt = (NBTBase) inputs[0];
        for (String ref : Tools.fixedSplit("" + inputs[1], ":"))
        {
            if (nbt instanceof NBTTagCompound) nbt = ((NBTTagCompound) nbt).getTag(ref);
            else throw new NotImplementedException("Have not yet added sub-accessors for " + nbt.getClass().getSimpleName());
        }

        if (nbt == null) return null;

        switch (nbt.getId())
        {
            case Constants.NBT.TAG_INT:
                return ((NBTTagInt) nbt).getInt();

            case Constants.NBT.TAG_DOUBLE:
                return ((NBTTagDouble) nbt).getDouble();

            case Constants.NBT.TAG_FLOAT:
                return ((NBTTagFloat) nbt).getFloat();

            case Constants.NBT.TAG_STRING:
                return ((NBTTagString) nbt).getString();

            case Constants.NBT.TAG_SHORT:
                return ((NBTTagShort) nbt).getShort();

            case Constants.NBT.TAG_BYTE:
                return ((NBTTagByte) nbt).getByte();

            case Constants.NBT.TAG_LONG:
                return ((NBTTagLong) nbt).getLong();

            default:
                return nbt;
        }
    }
}
