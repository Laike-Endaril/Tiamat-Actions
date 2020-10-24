package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.DamageTypes;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeDamageEntity extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/damage_entity.png");
    protected static final Pair<String, Class> OPTIONAL_INPUTS = new Pair<>("moreTargets", Entity.class);
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("damageType", String.class);
        REQUIRED_INPUTS.put("trueSource", Entity.class);
        REQUIRED_INPUTS.put("immediateSource", Entity.class);
        REQUIRED_INPUTS.put("damageAmount", Object.class);
        REQUIRED_INPUTS.put("target", Entity.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeDamageEntity()
    {
        super();
    }

    public CNodeDamageEntity(String actionName, String event, int x, int y)
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
        return "Damage one or more entities";
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
        DamageSource damageSource = DamageTypes.CUSTOM_DAMAGE_TYPES.get("" + inputs[0]);
        Entity trueSource = (Entity) inputs[1], immediateSource = (Entity) inputs[2];

        if (immediateSource != null)
        {
            damageSource = new EntityDamageSourceIndirect(damageSource.damageType, immediateSource, trueSource);
        }
        else if (trueSource != null)
        {
            damageSource = new EntityDamageSource(damageSource.damageType, trueSource);
        }

        float amount = Float.parseFloat("" + inputs[3]);

        for (int i = 4; i < inputs.length; i++)
        {
            Entity target = (Entity) inputs[i];
            target.attackEntityFrom(damageSource, amount);
        }

        return null;
    }
}
