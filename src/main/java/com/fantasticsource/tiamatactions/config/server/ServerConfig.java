package com.fantasticsource.tiamatactions.config.server;

import net.minecraftforge.common.config.Config;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class ServerConfig
{
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
                    "Main, 2, true",
                    "Spawn, 1, true"
            };

    @Config.Name("Spawn Actions")
    @Config.LangKey(MODID + ".config.spawnActions")
    @Config.Comment(
            {
                    "A list of actions to run when entities spawn.  Syntax is...",
                    "QueueName, ActionName",
            })
    @Config.RequiresMcRestart
    public String[] spawnActions = new String[0];

    @Config.Name("Forge EntityEvent Actions")
    @Config.LangKey(MODID + ".config.forgeEntityEventActions")
    @Config.Comment(
            {
                    "This lets you trigger server-side actions when a Forge EntityEvent occurs.  Syntax is...",
                    "full.package.and.ClassNameOfEntityEvent, ActionName, QueueName",
                    "",
                    "The entity involved in the EntityEvent is the one who will execute the action",
                    "The action argument will be the EntityEvent that triggered the action"
            })
    @Config.RequiresMcRestart
    public String[] forgeEntityEventActions = new String[0];
}
