package com.fantasticsource.tiamatactions.config;

import com.fantasticsource.tiamatactions.config.client.ClientConfig;
import com.fantasticsource.tiamatactions.config.server.ServerConfig;
import net.minecraftforge.common.config.Config;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

@Config(modid = MODID)
public class TiamatActionsConfig
{
    @Config.Name("Client Settings")
    @Config.LangKey(MODID + ".config.clientSettings")
    public static ClientConfig clientSettings = new ClientConfig();

    @Config.Name("Server Settings")
    @Config.LangKey(MODID + ".config.serverSettings")
    public static ServerConfig serverSettings = new ServerConfig();
}
