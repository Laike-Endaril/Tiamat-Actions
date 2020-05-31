package com.fantasticsource.tiamatactions;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.mctools.ServerTickTimer;
import com.fantasticsource.tiamatactions.action.ActionQueue;
import com.fantasticsource.tiamatactions.action.CAction;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

@Mod(modid = TiamatActions.MODID, name = TiamatActions.NAME, version = TiamatActions.VERSION, dependencies = "required-after:fantasticlib@[1.12.2.034j,)")
public class TiamatActions
{
    public static final String MODID = "tiamatactions";
    public static final String NAME = "Tiamat Actions";
    public static final String VERSION = "1.12.2.000a";


    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(TiamatActions.class);
        Network.init();
        Attributes.init();
        DamageTypes.init();
        MinecraftForge.EVENT_BUS.register(ActionQueue.class);
    }

    @SubscribeEvent
    public static void saveConfig(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(MODID)) ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public static void tick(TickEvent.ServerTickEvent event)
    {
        //TODO remove test code below
        if (event.phase != TickEvent.Phase.END) return;

        if (MCTools.devEnv() && ServerTickTimer.currentTick() % 20 == 0)
        {
            List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
            if (players.size() > 0)
            {
                CAction.ALL_ACTIONS.get("Test1").queue(players.get(0), "Main");
                CAction.ALL_ACTIONS.get("Test2").queue(players.get(0), "Main");
                CAction.ALL_ACTIONS.get("Test3").queue(players.get(0), "Main");
            }
        }
    }
}
