package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.potions.Potions;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeApplyPotionEffect extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/apply_potion_effect.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();
    protected static final Pair<String, Class> OPTIONAL_INPUTS = new Pair<>("interval", Object.class);

    static
    {
        REQUIRED_INPUTS.put("entity", Entity.class);
        REQUIRED_INPUTS.put("registryName", Object.class);
        REQUIRED_INPUTS.put("duration", Object.class);
        REQUIRED_INPUTS.put("level", Object.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeApplyPotionEffect()
    {
        super();
    }

    public CNodeApplyPotionEffect(String actionName, String event, int x, int y)
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
        return "Apply a potion effect to an entity";
    }


    @Override
    public LinkedHashMap<String, Class> getRequiredInputs()
    {
        return REQUIRED_INPUTS;
    }

    @Override
    public Pair<String, Class> getOptionalInputs()
    {
        return OPTIONAL_INPUTS;
    }

    @Override
    public Class outputType()
    {
        return null;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        ((EntityLivingBase) inputs[0]).addPotionEffect(Potions.parsePotion(inputs[1] + "." + inputs[2] + "." + inputs[3] + (inputs.length > 4 ? "." + inputs[4] : "")));
        return null;
    }
}
