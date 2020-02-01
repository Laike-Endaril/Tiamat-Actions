package com.fantasticsource.tiamatactions;

import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import net.minecraft.util.DamageSource;

import java.util.LinkedHashMap;

public class DamageTypes
{
    public static LinkedHashMap<String, DamageSource> CUSTOM_DAMAGE_TYPES = null;

    public static void init()
    {
        CUSTOM_DAMAGE_TYPES = new LinkedHashMap<>();
        for (String damageString : TiamatActionsConfig.serverSettings.customDamageTypes)
        {
            CUSTOM_DAMAGE_TYPES.put(damageString, new DamageSource(damageString));
        }
    }
}
