package com.fantasticsource.tiamatactions.action;

import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import com.fantasticsource.tools.Tools;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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

    protected void tick(Entity source)
    {
        Profiler profiler = source.world.profiler;
        profiler.startSection("Tiamat Action Queue: " + name);

        boolean entityDead = !source.isEntityAlive() || (!source.isAddedToWorld() && source.isDead);
        boolean queueTicked = false;
        while (queue.size() > 0)
        {
            CAction action = queue.get(0);
            if (!action.started)
            {
                if (entityDead)
                {
                    profiler.endSection();
                    return;
                }

                action.execute(source, "start");
                if (action.mainAction.active) action.started = true;
            }

            if (entityDead || action.tickEndpointNodes.size() == 0) action.mainAction.active = false;
            if (action.mainAction.active)
            {
                if (queueTicked)
                {
                    profiler.endSection();
                    return;
                }

                queueTicked = true;
                action.execute(source, "tick");
            }

            if (!action.mainAction.active)
            {
                if (action.mainAction.started) action.execute(source, "end");
                queue.remove(0);
            }
            else
            {
                profiler.endSection();
                return;
            }
        }

        profiler.endSection();
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

        ENTITY_ACTION_QUEUES.entrySet().removeIf(entry ->
        {
            Entity entity = entry.getKey();
            for (ActionQueue queue : entry.getValue().values()) queue.tick(entity);
            return !entity.isEntityAlive() || (!entity.isAddedToWorld() && entity.isDead);
        });
    }

    @SubscribeEvent
    public static void entityJoinWorld(EntityJoinWorldEvent event)
    {
        Entity entity = event.getEntity();
        if (entity.world.isRemote) return;

        for (String s : TiamatActionsConfig.serverSettings.spawnActions)
        {
            if (!entity.isEntityAlive()) continue;

            String[] tokens = Tools.fixedSplit(s, ",");
            CAction action = CAction.ALL_ACTIONS.get(tokens[1].trim());
            if (action == null) continue;

            action.queue(entity, tokens[0].trim(), null);
        }
    }
}
