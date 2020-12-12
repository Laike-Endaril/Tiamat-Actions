package com.fantasticsource.tiamatactions.config.modpack;

import net.minecraftforge.common.config.Config;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class ModpackConfig
{
    @Config.Name("Custom Attributes")
    @Config.LangKey(MODID + ".config.customAttributes")
    @Config.Comment(
            {
                    "A list of custom attributes to add to living entities",
                    "Syntax is...",
                    "name, defaultValue, parentName, minValue, maxValue",
                    "All arguments except name are optional, but must include all preceding arguments",
                    "The full attribute name will end up as tiamatactions.<name>, eg 'strength' would end up as 'tiamatactions.strength'",
                    "The parent name must be the (partial) name of another custom attribute (ie. 'strength', not 'tiamatactions.strength'"
            })
    @Config.RequiresMcRestart
    public String[] customAttributes = new String[0];

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
