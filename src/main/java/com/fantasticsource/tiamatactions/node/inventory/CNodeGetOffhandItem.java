package com.fantasticsource.tiamatactions.node.inventory;

import com.fantasticsource.mctools.GlobalInventory;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeGetOffhandItem extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/get_offhand_item.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("entity", Entity.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeGetOffhandItem()
    {
        super();
    }

    public CNodeGetOffhandItem(String actionName, String event, int x, int y)
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
        return "Get an entity's current offhand item";
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
        return ItemStack.class;
    }


    @Override
    public Object execute(CAction mainAction, Object... inputs)
    {
        ArrayList<ItemStack> result = GlobalInventory.getVanillaOffhandItems((Entity) inputs[0]);
        return result.size() == 0 ? null : result.get(0);
    }
}
