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
                    "All arguments except name are optional, but must include all preceding arguments"
            })
    @Config.RequiresMcRestart
    public String[] customAttributes = new String[0];

    @Config.Name("Keybound Actions")
    @Config.LangKey(MODID + ".config.keyboundActions")
    @Config.Comment(
            {
                    "A list of actions that can be run by a player via keybind",
                    "Syntax is...",
                    "keybindName;actionName",
                    "",
                    "Eg...",
                    "test;TestAction",
                    "...would make a keybind with the translation key tiamatactions.key.test, which would run an action named TestAction"
            })
    @Config.RequiresMcRestart
    public String[] keyboundActions = new String[0];
}
