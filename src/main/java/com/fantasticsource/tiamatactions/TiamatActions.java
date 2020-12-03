package com.fantasticsource.tiamatactions;

import com.fantasticsource.fantasticlib.api.FLibAPI;
import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tiamatactions.action.ActionQueue;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.block.BlocksAndItems;
import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import com.fantasticsource.tiamatactions.gui.actioneditor.GUINodeView;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tiamatactions.trigger.PlayerEventActionTrigger;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Mod(modid = TiamatActions.MODID, name = TiamatActions.NAME, version = TiamatActions.VERSION, dependencies = "required-after:fantasticlib@[1.12.2.043b,);after:tiamathud")
public class TiamatActions
{
    public static final String MODID = "tiamatactions";
    public static final String NAME = "Tiamat Actions";
    public static final String VERSION = "1.12.2.000zzb";
    public static final String DOMAIN = "tiamatrpg";

    protected static final String IN_JAR_PATH = "assets/" + MODID + "/";

    private static final ScriptEngineManager SCRIPT_ENGINE_MANAGER = new ScriptEngineManager();
    public static final ScriptEngine JAVASCRIPT_ENGINE = SCRIPT_ENGINE_MANAGER.getEngineByName("JavaScript");

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
        printMissingTextures();

        FLibAPI.attachNBTCapToEntityIf(MODID, (o) -> true);

        MinecraftForge.EVENT_BUS.register(TiamatActions.class);
        Network.init();
        Attributes.init();
        DamageTypes.refresh();
        MinecraftForge.EVENT_BUS.register(EntityVars.class);
        MinecraftForge.EVENT_BUS.register(ActionQueue.class);
        MinecraftForge.EVENT_BUS.register(BlocksAndItems.class);

        CAction.reloadAll();

        if (TiamatActionsConfig.serverSettings.forgePlayerEventActions.length > 0) MinecraftForge.EVENT_BUS.register(PlayerEventActionTrigger.class);


        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            Keys.init(event);
            MinecraftForge.EVENT_BUS.register(TooltipAlterer.class);
        }
    }

    @SubscribeEvent
    public static void saveConfig(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(MODID)) ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public static void syncConfig(ConfigChangedEvent.PostConfigChangedEvent event)
    {
        DamageTypes.refresh();
    }

    @Mod.EventHandler
    public static void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new Commands());
    }

    public static void printMissingTextures()
    {
        ArrayList<String> missing = new ArrayList<>();
        try
        {
            for (Class<? extends CNode> nodeClass : GUINodeView.getNodeClasses())
            {
                String subPath = nodeClass.newInstance().getTexture().getResourcePath();
                InputStream stream = MCTools.getJarResourceStream(TiamatActions.class, IN_JAR_PATH + subPath);
                if (stream == null) missing.add(subPath);
                else stream.close();
            }
        }
        catch (InstantiationException | IllegalAccessException | IOException e)
        {
            e.printStackTrace();
        }

        if (missing.size() > 0)
        {
            System.out.println();
            System.out.println(TextFormatting.RED + "MISSING NODE TEXTURES...");
            for (String s : missing) System.out.println(TextFormatting.RED + s);
            System.out.println();
        }
    }
}
