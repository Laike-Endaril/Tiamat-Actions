package com.fantasticsource.tiamatactions;

import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import com.fantasticsource.tools.Tools;
import net.minecraft.util.DamageSource;

import java.util.LinkedHashMap;

public class DamageTypes
{
    public static LinkedHashMap<String, DamageSource> CUSTOM_DAMAGE_TYPES = null;

    public static void refresh()
    {
        CUSTOM_DAMAGE_TYPES = new LinkedHashMap<>();

        for (String s : TiamatActionsConfig.serverSettings.customDamageTypes)
        {
            String[] tokens = Tools.fixedSplit(s, ",");

            if (tokens.length < 2) continue;

            String name = tokens[0];
            String key = tokens[1];
            if (name.equals("") || key.equals("")) continue;

            DamageSource damageSource = new DamageSource(key);

            for (int i = 2; i < tokens.length; i++)
            {
                switch (tokens[i].trim().toLowerCase())
                {
                    case "bypassarmor":
                        damageSource.setDamageBypassesArmor();

                    case "absolute":
                        damageSource.setDamageIsAbsolute();

                    case "creative":
                        damageSource.setDamageAllowedInCreativeMode();

                    case "difficultyscaled":
                        damageSource.setDifficultyScaled();

                    case "explosion":
                        damageSource.setExplosion();

                    case "fire":
                        damageSource.setFireDamage();

                    case "magic":
                        damageSource.setMagicDamage();

                    case "projectile":
                        damageSource.setProjectile();
                }
            }

            CUSTOM_DAMAGE_TYPES.put(name, damageSource);
        }
    }
}
