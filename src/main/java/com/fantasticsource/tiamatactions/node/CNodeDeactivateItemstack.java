package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.DOMAIN;
import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeDeactivateItemstack extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/deactivate_itemstack.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("itemstack", ItemStack.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeDeactivateItemstack()
    {
        super();
    }

    public CNodeDeactivateItemstack(String actionName, String event, int x, int y)
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
        return "Deactivate an itemstack";
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
        ItemStack stack = (ItemStack) inputs[0];
        if (stack == null) return null;

        if (!stack.hasTagCompound()) return null;

        NBTTagCompound mainTag = stack.getTagCompound();
        if (!mainTag.hasKey(DOMAIN)) return null;

        NBTTagCompound compound = mainTag.getCompoundTag(DOMAIN);
        if (!compound.hasKey("active")) return null;

        compound.removeTag("active");
        if (compound.hasNoTags())
        {
            mainTag.removeTag(DOMAIN);
            if (mainTag.hasNoTags()) stack.setTagCompound(null);
        }

        return null;
    }
}
