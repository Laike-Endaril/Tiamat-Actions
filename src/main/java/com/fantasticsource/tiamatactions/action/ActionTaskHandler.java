package com.fantasticsource.tiamatactions.action;

import com.fantasticsource.tiamatactions.task.CTask;
import com.fantasticsource.tiamatactions.task.CTaskCommand;
import com.fantasticsource.tiamatactions.task.CTaskDelay;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ActionTaskHandler //TODO extend component
{
    public static final LinkedHashMap<ICommandSender, ActionTaskHandler> CONTROLLER_DATA = new LinkedHashMap<>();


    //Does not change
    public final ICommandSender controller;


    //Change per-action
    public CAction currentAction = null, queuedAction = null;
    public ItemStack currentActivatedItem = null, queuedActivatingItem = null;

    //Retained between actions, but contents change per-task
    public final LinkedList<CTask> currentTasks = new LinkedList<>(), queuedTasks = new LinkedList<>();
    public final LinkedHashMap<String, Object> entityVars = new LinkedHashMap<>();


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
            if (queuedAction != null) startAction(queuedAction, queuedActivatingItem);
        }

        while (currentTasks.size() > 0) currentTasks.pop().tick(this);
    }


    public void queueAction(CAction action, ItemStack activatingItem)
    {
        if (currentAction == null) startAction(action, activatingItem);
        else
        {
            queuedAction = action;
            queuedActivatingItem = activatingItem;
        }
    }

    protected void endAction(CAction action)
    {
        currentAction = null;
        currentActivatedItem = null;

        currentTasks.removeIf(task -> task.stopOnActionInterrupt);
        queuedTasks.removeIf(task -> task.stopOnActionInterrupt);
    }

    protected void startAction(CAction action, ItemStack activatingItem)
    {
        if (queuedAction == action)
        {
            queuedAction = null;
            queuedActivatingItem = null;
        }

        currentAction = action;
        currentActivatedItem = activatingItem;

        if (action.tasks.size() > 0) currentTasks.add(action.getTaskChain(0));
    }


    public static void queueAction(ICommandSender controller, CAction action, @Nullable ItemStack activatingItem)
    {
        CONTROLLER_DATA.get(controller).queueAction(action, activatingItem);
    }


    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        CONTROLLER_DATA.put(event.player, new ActionTaskHandler(event.player));
        //TODO load ActionTaskHandler for player
    }

    @SubscribeEvent
    public static void playerLogout(PlayerEvent.PlayerLoggedOutEvent event)
    {
        //TODO save ActionTaskHandler for player
        CONTROLLER_DATA.remove(event.player);
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.side == Side.CLIENT || event.phase != TickEvent.Phase.END) return;


        //Server-side, player end tick only
        for (ActionTaskHandler handler : CONTROLLER_DATA.values()) handler.tick();
    }


    public static void serverStart(FMLServerStartingEvent event)
    {
        //TODO load actions


        //TODO start test code
        CAction testAction = CAction.getInstance("Test");

        CTaskCommand commandTask = new CTaskCommand();
        commandTask.owningAction = testAction.name;
        commandTask.command = "/time set 13000";
        testAction.tasks.add(commandTask);

        CTaskDelay delayTask = new CTaskDelay();
        delayTask.owningAction = testAction.name;
        delayTask.delay = 20;
        testAction.tasks.add(delayTask);

        commandTask = new CTaskCommand();
        commandTask.owningAction = testAction.name;
        commandTask.command = "/time set 0";
        testAction.tasks.add(commandTask);
        //TODO end test code


        CONTROLLER_DATA.put(event.getServer(), new ActionTaskHandler(event.getServer()));
        //TODO load ActionTaskHandler for server
    }

    public static void serverStop(FMLServerStoppedEvent event)
    {
        CONTROLLER_DATA.remove(FMLCommonHandler.instance().getMinecraftServerInstance());
        //TODO load ActionTaskHandler for server
    }
}
