package com.fantasticsource.tiamatactions.config.modpack;

import net.minecraftforge.common.config.Config;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class ModpackConfig
{
    @Config.Name("Keybound Actions")
    @Config.LangKey(MODID + ".config.keyboundActions")
    @Config.Comment(
            {
                    "A list of actions that can be run by a player via keybind",
                    "Syntax is...",
                    "keybindName;ActionName;QueueName",
                    "QueueName is optional.  Uses 'Main' queue if omitted or empty.  Uses a null queue ('execute immediate' style) if you enter the word 'null'",
                    "",
                    "Eg...",
                    "test;TestAction",
                    "...would make a keybind with the translation key tiamatactions.key.test, which would run an action named TestAction in an action queue named Main"
            })
    @Config.RequiresMcRestart
    public String[] keyboundActions = new String[0];
}
