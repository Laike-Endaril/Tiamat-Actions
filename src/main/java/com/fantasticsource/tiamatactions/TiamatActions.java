package com.fantasticsource.tiamatactions;

import com.fantasticsource.tiamatactions.action.ActionQueue;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.block.BlocksAndItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Mod(modid = TiamatActions.MODID, name = TiamatActions.NAME, version = TiamatActions.VERSION, dependencies = "required-after:fantasticlib@[1.12.2.034zg,)")
public class TiamatActions
{
    public static final String MODID = "tiamatactions";
    public static final String NAME = "Tiamat Actions";
    public static final String VERSION = "1.12.2.000e";


    private static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
    public static final ScriptEngine JAVASCRIPT_ENGINE = SCRIPT_ENGINE_MANAGER.getEngineByName("JavaScript");

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(TiamatActions.class);
        Network.init();
        Attributes.init();
        DamageTypes.init();
        MinecraftForge.EVENT_BUS.register(ActionQueue.class);
        MinecraftForge.EVENT_BUS.register(BlocksAndItems.class);

        CAction.loadAll();


        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            Keys.init(event);
        }
    }

    @SubscribeEvent
    public static void saveConfig(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(MODID)) ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }
}
