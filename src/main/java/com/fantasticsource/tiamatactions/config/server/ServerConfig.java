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
}
