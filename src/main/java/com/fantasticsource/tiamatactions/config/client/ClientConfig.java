package com.fantasticsource.tiamatactions.config.client;

import net.minecraftforge.common.config.Config;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class ClientConfig
{
    @Config.Name("GUI Settings")
    @Config.LangKey(MODID + ".config.guiSettings")
    public GUIConfig guiSettings = new GUIConfig();
}
