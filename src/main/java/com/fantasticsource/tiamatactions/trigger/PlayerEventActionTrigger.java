package com.fantasticsource.tiamatactions.trigger;

import com.fantasticsource.tiamatactions.action.ActionQueue;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import com.fantasticsource.tools.ReflectionTool;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerEventActionTrigger
{
    public static final HashMap<Class<? extends PlayerEvent>, ArrayList<Pair<String, String>>> PLAYER_EVENT_ACTIONS = new HashMap<>();

    static
    {
        for (String s : TiamatActionsConfig.serverSettings.forgePlayerEventActions)
        {
            String[] tokens = Tools.fixedSplit(s, ",");
            if (tokens.length < 3)
            {
                System.err.println(TextFormatting.RED + "Invalid Forge PlayerEvent action trigger: " + s);
                continue;
            }

            Class cls = ReflectionTool.getClassByName(tokens[0].trim());
            if (!PlayerEvent.class.isAssignableFrom(cls))
            {
                System.err.println(TextFormatting.RED + "Class for Forge PlayerEvent action trigger is not a subclass of PlayerEvent: " + s);
                continue;
            }

            String actionName = tokens[1].trim(), queueName = tokens[2].trim();
            if (queueName.equals("") || queueName.equalsIgnoreCase("null")) queueName = null;
            else if (!ActionQueue.queueExists(queueName))
            {
                System.err.println(TextFormatting.RED + "Queue for Forge PlayerEvent action trigger does not exist: " + s);
                continue;
            }

            PLAYER_EVENT_ACTIONS.computeIfAbsent(cls, o -> new ArrayList<>()).add(new Pair<>(actionName, queueName));
        }
    }

    @SubscribeEvent
    public static void playerEvent(PlayerEvent playerEvent)
    {
        EntityPlayer player = playerEvent.getEntityPlayer();
        if (!(player instanceof EntityPlayerMP)) return;

        Class<? extends PlayerEvent> eventClass = playerEvent.getClass();
        ArrayList<Pair<String, String>> pairs = PLAYER_EVENT_ACTIONS.get(eventClass);
        if (pairs == null) return;


        for (Pair<String, String> pair : pairs)
        {
            CAction action = CAction.ALL_ACTIONS.get(pair.getKey());
            if (action == null) continue;

            action.queue(player, pair.getValue(), null, eventClass);
        }
    }
}
