package com.fantasticsource.tiamatactions.action;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import com.fantasticsource.tools.ReflectionTool;
import com.fantasticsource.tools.Tools;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.NAME;

public class ActionQueue
{
    protected static final LinkedHashMap<Entity, LinkedHashMap<String, ActionQueue>> ENTITY_ACTION_QUEUES = new LinkedHashMap<>();

    public String name;
    public int size;
    public boolean replaceLastIfFull, closing = false;
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
        boolean profile = TiamatActionsConfig.serverSettings.profilingMode.equals("actions");
        if (profile) profiler.startSection("Queue: " + name);

        boolean queueTicked = false;
        while (queue.size() > 0)
        {
            CAction action = queue.get(0);
            if (!action.started)
            {
                if (closing) break;

                action.execute(source, "start");
                if (action.mainAction.active) action.started = true;
            }

            if (closing || action.tickEndpointNodes.size() == 0) action.mainAction.active = false;
            if (action.mainAction.active)
            {
                if (queueTicked) break;

                queueTicked = true;
                action.execute(source, "tick");
            }

            if (!action.mainAction.active)
            {
                if (action.mainAction.started) action.execute(source, "end");
                queue.remove(0);
                if (closing) break;
            }
            else break;
        }

        if (profile) profiler.endSection();
    }

    public static ArrayList<String> existingQueues()
    {
        ArrayList<String> queueNames = new ArrayList<>();
        for (String line : TiamatActionsConfig.serverSettings.actionQueues)
        {
            String[] tokens = Tools.fixedSplit(line, ",");
            if (tokens.length != 3)
            {
                throw new IllegalArgumentException("Bad syntax in queue config: " + line);
            }

            try
            {
                Integer.parseInt(tokens[1].trim());
            }
            catch (NumberFormatException e)
            {
                throw new IllegalArgumentException("Bad syntax in queue config: " + line);
            }

            queueNames.add(tokens[0].trim());
        }
        return queueNames;
    }

    public static boolean queueExists(String queueName)
    {
        return existingQueues().contains(queueName);
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

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        server.profiler.startSection(NAME + ": Tick action queues");

        Entity[] entities = ENTITY_ACTION_QUEUES.keySet().toArray(new Entity[0]);
        LinkedHashMap<String, ActionQueue>[] queueSets = ENTITY_ACTION_QUEUES.values().toArray(new LinkedHashMap[0]);
        for (int i = 0; i < entities.length; i++)
        {
            Entity entity = entities[i];
            LinkedHashMap<String, ActionQueue> queueSet = queueSets[i];

            boolean close = !entity.isEntityAlive() || !MCTools.entityIsValid(entity);

            for (ActionQueue queue : queueSet.values())
            {
                if (close) queue.closing = true;
                queue.tick(entity);
            }

            if (close) ENTITY_ACTION_QUEUES.remove(entity);
        }

        server.profiler.endSection();
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
            if (tokens.length > 2)
            {
                if (!ReflectionTool.getClassByName(tokens[2].trim()).isAssignableFrom(entity.getClass())) continue;
            }

            CAction action = CAction.ALL_ACTIONS.get(tokens[1].trim());
            if (action == null) continue;

            action.queue(entity, tokens[0].trim(), null);
        }
    }
}
