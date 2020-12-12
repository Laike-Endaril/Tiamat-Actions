package com.fantasticsource.tiamatactions;

import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import com.fantasticsource.tools.Tools;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

@SideOnly(Side.CLIENT)
public class Keys
{
    protected static final String PREFIX = MODID + ".key.";
    protected static final ArrayList<KeyBinding> KEY_BINDINGS = new ArrayList<>();

    public static void init(FMLPreInitializationEvent event)
    {
        for (String s : TiamatActionsConfig.modpackSettings.keyboundActions)
        {
            String[] tokens = Tools.fixedSplit(s, ";");
            if (tokens.length < 2) continue;


            KeyBinding keyBinding = new KeyBinding(PREFIX + tokens[0].trim(), KeyConflictContext.UNIVERSAL, Keyboard.KEY_NONE, MODID + ".keyCategory");
            KEY_BINDINGS.add(keyBinding);
            ClientRegistry.registerKeyBinding(keyBinding);
        }

        MinecraftForge.EVENT_BUS.register(Keys.class);
    }

    @SubscribeEvent
    public static void keyPress(InputEvent event)
    {
        for (KeyBinding keyBinding : KEY_BINDINGS)
        {
            if (keyBinding.isPressed())
            {
                Network.WRAPPER.sendToServer(new Network.ExecuteKeyboundActionPacket(keyBinding.getKeyDescription().replace(PREFIX, "")));
                break;
            }
        }
    }
}
