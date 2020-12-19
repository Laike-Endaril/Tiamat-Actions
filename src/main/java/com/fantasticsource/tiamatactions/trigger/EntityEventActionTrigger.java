package com.fantasticsource.tiamatactions.trigger;

import com.fantasticsource.tiamatactions.action.ActionQueue;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import com.fantasticsource.tools.ReflectionTool;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityEventActionTrigger
{
    public static final HashMap<Class<? extends EntityEvent>, ArrayList<Pair<String, String>>> ENTITY_EVENT_ACTIONS = new HashMap<>();

    static
    {
        for (String s : TiamatActionsConfig.serverSettings.forgeEntityEventActions)
        {
            String[] tokens = Tools.fixedSplit(s, ",");
            if (tokens.length < 3)
            {
                System.err.println(TextFormatting.RED + "Invalid Forge EntityEvent action trigger: " + s);
                continue;
            }

            Class cls = ReflectionTool.getClassByName(tokens[0].trim());
            if (!EntityEvent.class.isAssignableFrom(cls))
            {
                System.err.println(TextFormatting.RED + "Class for Forge EntityEvent action trigger is not a subclass of EntityEvent: " + s);
                continue;
            }

            String actionName = tokens[1].trim(), queueName = tokens[2].trim();
            if (queueName.equals("") || queueName.equalsIgnoreCase("null")) queueName = null;
            else if (!ActionQueue.queueExists(queueName))
            {
                System.err.println(TextFormatting.RED + "Queue for Forge EntityEvent action trigger does not exist: " + s);
                continue;
            }

            ENTITY_EVENT_ACTIONS.computeIfAbsent(cls, o -> new ArrayList<>()).add(new Pair<>(actionName, queueName));
        }
    }

    @SubscribeEvent
    public static void entityEvent(EntityEvent entityEvent)
    {
        Entity entity = entityEvent.getEntity();
        if (!(entity instanceof EntityPlayerMP)) return;

        Class<? extends EntityEvent> eventClass = entityEvent.getClass();
        ArrayList<Pair<String, String>> pairs = ENTITY_EVENT_ACTIONS.get(eventClass);
        if (pairs == null) return;


        for (Pair<String, String> pair : pairs)
        {
            CAction action = CAction.ALL_ACTIONS.get(pair.getKey());
            if (action == null) continue;

            action.queue(entity, pair.getValue(), null, entityEvent);
        }
    }
}
