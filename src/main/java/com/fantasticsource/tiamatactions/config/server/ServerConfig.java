package com.fantasticsource.tiamatactions.config.server;

import net.minecraftforge.common.config.Config;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class ServerConfig
{
    @Config.Name("Custom Attributes")
    @Config.LangKey(MODID + ".config.customAttributes")
    @Config.Comment("A list of custom attributes to add to living entities")
    @Config.RequiresMcRestart
    public String[] customAttributes = new String[0];

    @Config.Name("Custom Damage Types")
    @Config.LangKey(MODID + ".config.customDamageTypes")
    @Config.Comment(
            {
                    "A list of custom damage types to add to the game",
                    "Syntax is...",
                    "Name, translationKey, option1, option2, option3...",
                    "",
                    "Any number of optional arguments can be added to an entry.  Optional arguments are...",
                    "bypassArmor",
                    "absolute",
                    "creative",
                    "difficultyScaled",
                    "explosion",
                    "fire",
                    "magic",
                    "projectile"
            })
    public String[] customDamageTypes = new String[0];

    @Config.Name("Action Queues")
    @Config.LangKey(MODID + ".config.actionQueues")
    @Config.Comment(
            {
                    "A list of action queues to add to entities.  Syntax is...",
                    "Name, size, replaceLastIfFull",
                    "Eg...",
                    "Main, 2, true"
            })
    @Config.RequiresMcRestart
    public String[] actionQueues = new String[]
            {
                    "Main, 2, true"
            };
}
