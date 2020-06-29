package com.fantasticsource.tiamatactions.config.modpack;

import net.minecraftforge.common.config.Config;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class ModpackConfig
{
    @Config.Name("Keybound Actions")
    @Config.LangKey(MODID + ".config.keyboundActions")
    @Config.Comment("A list of actions that can be run by a player via keybind")
    @Config.RequiresMcRestart
    public String[] keyboundActions = new String[0];
}
