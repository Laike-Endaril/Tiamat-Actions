package com.fantasticsource.tiamatactions.task;

import com.fantasticsource.tiamatactions.action.ActionTaskHandler;
import com.fantasticsource.tiamatactions.action.Action;
import com.fantasticsource.tiamatactions.gui.TaskGUI;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TaskCommand extends Task
{
    protected String command = "";


    public TaskCommand(Action action)
    {
        super(action);
    }


    @Override
    public String getDescription()
    {
        return "Run Command: " + command;
    }

    @Override
    public void tick(ActionTaskHandler handler)
    {
        FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(handler.controller, command.replaceAll("@p|@P", handler.controller.getName()));
        handler.currentTasks.addAll(nextTasks);
    }

    @Override
    public TaskGUI getTaskGUI()
    {
        //TODO
        return null;
    }
}
