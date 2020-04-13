package com.fantasticsource.tiamatactions;

import com.fantasticsource.tiamatactions.action.ActionTaskHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = TiamatActions.MODID, name = TiamatActions.NAME, version = TiamatActions.VERSION, dependencies = "required-after:fantasticlib@[1.12.2.032b,)")
public class TiamatActions
{
    public static final String MODID = "tiamatactions";
    public static final String NAME = "Tiamat Actions";
    public static final String VERSION = "1.12.2.000";


    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(TiamatActions.class);
        MinecraftForge.EVENT_BUS.register(BlocksAndItems.class);
        MinecraftForge.EVENT_BUS.register(ActionTaskHandler.class);
        MinecraftForge.EVENT_BUS.register(Attributes.class);
        Network.init();
        Attributes.init();
        DamageTypes.init();
    }

    @SubscribeEvent
    public static void saveConfig(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(MODID)) ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }


    @Mod.EventHandler
    public static void serverStart(FMLServerStartingEvent event)
    {
        ActionTaskHandler.serverStart(event);
    }

    @Mod.EventHandler
    public static void serverStop(FMLServerStoppedEvent event)
    {
        ActionTaskHandler.serverStop(event);
    }

    //TODO fix action system
//    @SubscribeEvent
//    public static void test(TickEvent.PlayerTickEvent event)
//    {
//        if (event.side == Side.CLIENT || event.phase != TickEvent.Phase.END || ServerTickTimer.currentTick() % 120 != 119) return;
//
//        ActionTaskHandler.queueAction(event.player, CAction.allActions.get("Test"), null);
//    }
}
