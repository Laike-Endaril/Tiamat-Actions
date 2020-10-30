package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.DOMAIN;
import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeActivateItemstack extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/is_itemstack_active.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("itemstack", ItemStack.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeActivateItemstack()
    {
        super();
    }

    public CNodeActivateItemstack(String actionName, String event, int x, int y)
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
        return "Check whether an itemstack is active";
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
        ItemStack stack = (ItemStack) inputs[0];
        if (stack == null) return false;

        if (!stack.hasTagCompound()) return false;

        NBTTagCompound compound = stack.getTagCompound();
        if (!compound.hasKey(DOMAIN)) return false;

        compound = compound.getCompoundTag(DOMAIN);
        if (!compound.hasKey("active")) return false;

        return compound.getBoolean("active");
    }
}
