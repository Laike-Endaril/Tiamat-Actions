package com.fantasticsource.tiamatactions.action;

import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import com.fantasticsource.tools.Tools;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ActionQueue
{
    protected static final LinkedHashMap<Entity, LinkedHashMap<String, ActionQueue>> ENTITY_ACTION_QUEUES = new LinkedHashMap<>();

    public String name;
    int size;
    boolean replaceLastIfFull;
    public ArrayList<CAction> queue = new ArrayList<>();

    public ActionQueue(String name, int size, boolean replaceLastIfFull)
    {
        this.name = name;
        this.size = size;
        this.replaceLastIfFull = replaceLastIfFull;
    }

    protected void tick()
    {
        boolean queueTicked = false;
        while (queue.size() > 0)
        {
            CAction action = queue.get(0);
            if (!action.started)
            {
                action.execute("start");
                if (action.mainAction.active) action.started = true;
            }

            if (action.tickEndpointNodes.size() == 0) action.mainAction.active = false;
            if (action.mainAction.active)
            {
                if (queueTicked) return;

                queueTicked = true;
                action.execute("tick");
            }

            if (!action.mainAction.active)
            {
                if (action.mainAction.started) action.execute("end");
                queue.remove(0);
            }
            else return;
        }
    }

    public static ActionQueue get(Entity entity, String queueName)
    {
        for (String line : TiamatActionsConfig.serverSettings.actionQueues)
        {
            String[] tokens = Tools.fixedSplit(line, ",");
            if (tokens.length != 3)
            {
                throw new IllegalArgumentException("Bad syntax in queue config: " + line);
            }

            int size;
            try
            {
                size = Integer.parseInt(tokens[1].trim());
            }
            catch (NumberFormatException e)
            {
                throw new IllegalArgumentException("Bad syntax in queue config: " + line);
            }


            if (!tokens[0].trim().equals(queueName)) continue;


            LinkedHashMap<String, ActionQueue> map = ENTITY_ACTION_QUEUES.computeIfAbsent(entity, o -> new LinkedHashMap<>());
            String r = tokens[2].trim();
            return map.computeIfAbsent(queueName, o -> new ActionQueue(queueName, size, r.toLowerCase().equals("t") || r.toLowerCase().equals("true")));
        }

        throw new IllegalArgumentException("Queue does not exist: " + queueName);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void tick(TickEvent.ServerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END || ENTITY_ACTION_QUEUES == null) return;

        //TODO Strange behavior using entrySet.removeIf(), and an alternate method using entrySet.toArray() didn't work right either...need to figure out why this crashes
        ENTITY_ACTION_QUEUES.entrySet().removeIf(entry ->
        {
            Entity entity = entry.getKey();
            if (!entity.isAddedToWorld() && entity.isDead) return true;
            if (!entity.isEntityAlive()) return true;

            for (ActionQueue queue : entry.getValue().values()) queue.tick();

            return false;
        });
    }
}
