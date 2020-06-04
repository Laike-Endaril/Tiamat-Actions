package com.fantasticsource.tiamatactions.action;

import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import com.fantasticsource.tiamatactions.task.CTask;
import com.fantasticsource.tools.Tools;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ActionQueue
{
    public static final LinkedHashMap<Entity, LinkedHashMap<String, ActionQueue>> ENTITY_ACTION_QUEUES = new LinkedHashMap<>();

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
        boolean ticked = false;
        while (queue.size() > 0)
        {
            CAction action = queue.get(0);
            if (!action.started)
            {
                for (CTask task : action.startTasks)
                {
                    task.execute(action);
                    if (!action.valid) break;
                }

                if (action.valid) action.started = true;
            }

            if (action.tickTasks.size() == 0) action.valid = false;
            if (action.valid)
            {
                if (ticked) return;


                ticked = true;
                for (CTask task : action.tickTasks)
                {
                    task.execute(action);
                    if (!action.valid) break;
                }
            }

            if (!action.valid)
            {
                if (action.started)
                {
                    for (CTask task : action.endTasks) task.execute(action);
                }

                queue.remove(0);
            }
            else return;
        }
    }

    @SubscribeEvent
    public static void entityConstructing(EntityEvent.EntityConstructing event)
    {
        LinkedHashMap<String, ActionQueue> map = new LinkedHashMap<>();
        ENTITY_ACTION_QUEUES.put(event.getEntity(), map);
        int size;
        for (String line : TiamatActionsConfig.serverSettings.actionQueues)
        {
            String[] tokens = Tools.fixedSplit(line, ",");
            if (tokens.length != 3)
            {
                throw new IllegalArgumentException("Bad syntax in queue config: " + line);
            }

            try
            {
                size = Integer.parseInt(tokens[1].trim());
            }
            catch (NumberFormatException e)
            {
                throw new IllegalArgumentException("Bad syntax in queue config: " + line);
            }

            String r = tokens[2].trim();
            String name = tokens[0].trim();
            map.put(name, new ActionQueue(name, size, r.toLowerCase().equals("t") || r.toLowerCase().equals("true")));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void tick(TickEvent.ServerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END) return;

        //entrySet.removeIf() caused very strange behavior.  I'm not sure if this approach actually fixes anything or if it merely hides the issue
        for (Object o : ENTITY_ACTION_QUEUES.entrySet().toArray())
        {
            Map.Entry<Entity, LinkedHashMap<String, ActionQueue>> entry = (Map.Entry<Entity, LinkedHashMap<String, ActionQueue>>) o;

            if (!entry.getKey().isAddedToWorld() && entry.getKey().isDead)
            {
                ENTITY_ACTION_QUEUES.remove(entry.getKey());
                continue;
            }

            for (ActionQueue queue : entry.getValue().values()) queue.tick();
        }
    }
}
