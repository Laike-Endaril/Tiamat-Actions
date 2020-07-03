package com.fantasticsource.tiamatactions.node.inventory.tiamat;

import com.fantasticsource.mctools.GlobalInventory;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeGetUtilitySkillItem extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/get_utility_skill_item.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("entity", Entity.class);
        REQUIRED_INPUTS.put("slot", String.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeGetUtilitySkillItem()
    {
        super();
    }

    public CNodeGetUtilitySkillItem(String actionName, String event, int x, int y)
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
        return "Get an entity's current utility skill item";
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
        return GlobalInventory.getTiamatUtilitySkills((Entity) inputs[0]).get(Integer.parseInt("" + inputs[1]) + 1);
    }
}
