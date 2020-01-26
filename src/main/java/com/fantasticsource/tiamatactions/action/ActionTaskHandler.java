package com.fantasticsource.tiamatactions.action;

import com.fantasticsource.tiamatactions.task.Task;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ActionTaskHandler
{
    public static final LinkedHashMap<ICommandSender, ActionTaskHandler> CONTROLLER_DATA = new LinkedHashMap<>();


    //Does not change
    public final ICommandSender controller;


    //Change per-action
    public Action currentAction = null, queuedAction = null;
    public ItemStack currentActivatedItem = null, queuedActivatingItem = null;

    //Retained between actions, but contents change per-task
    public final LinkedList<Task> currentTasks = new LinkedList<>(), queuedTasks = new LinkedList<>();
    public final LinkedHashMap<String, Object> vars = new LinkedHashMap<>();


    public ActionTaskHandler(ICommandSender controller)
    {
        this.controller = controller;
    }


    public void tick()
    {
        currentTasks.addAll(queuedTasks);
        queuedTasks.clear();

        while (currentTasks.size() == 0 && (currentAction != null || queuedAction != null))
        {
            endAction(currentAction);
            if (queuedAction != null) startAction(queuedAction);
        }

        while (currentTasks.size() > 0) currentTasks.pop().tick(this);
    }


    public void queueAction(Action action, ItemStack activatingItem)
    {
        if (currentAction == null) startAction(action);
        else
        {
            queuedAction = action;
            queuedActivatingItem = activatingItem;
        }
    }

    protected void endAction(Action action)
    {
        //TODO
    }

    protected void startAction(Action action)
    {
        //TODO
    }


    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        CONTROLLER_DATA.put(event.player, new ActionTaskHandler(event.player));
    }

    @SubscribeEvent
    public static void playerLogout(PlayerEvent.PlayerLoggedOutEvent event)
    {
        CONTROLLER_DATA.remove(event.player);
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.side == Side.CLIENT || event.phase != TickEvent.Phase.END) return;


        //Server-side, player end tick only
        for (ActionTaskHandler handler : CONTROLLER_DATA.values()) handler.tick();
    }
}
