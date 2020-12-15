package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Pair;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeGetItemstackAttributeModTotal extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/get_itemstack_attribute_mod_total.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("itemstack", ItemStack.class);
        REQUIRED_INPUTS.put("attribute", Object.class);
        REQUIRED_INPUTS.put("operation", Object.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeGetItemstackAttributeModTotal()
    {
        super();
    }

    public CNodeGetItemstackAttributeModTotal(String actionName, String event, int x, int y)
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
        return "Get an itemstack's attribute modifier total for a given attribute and operation";
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
        return String.class;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        ItemStack stack = (ItemStack) inputs[0];
        int operation = Integer.parseInt("" + inputs[2]);

        Multimap<String, AttributeModifier> allMods = null;
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values())
        {
            allMods = stack.getAttributeModifiers(slot);
            if (allMods.size() > 0) break;
        }
        if (allMods.size() == 0) return null; //Never NPE from size check

        Collection<AttributeModifier> mods = allMods.get("" + inputs[1]);
        if (mods == null || mods.size() == 0) return null;


        double amount = operation == 2 ? 1 : 0;
        for (AttributeModifier modifier : mods)
        {
            if (modifier.getOperation() == operation)
            {
                if (operation == 2) amount *= modifier.getAmount();
                else amount += modifier.getAmount();
            }
        }


        if (operation == 2) return amount == 1 ? null : amount;
        return amount == 0 ? null : amount;
    }
}
