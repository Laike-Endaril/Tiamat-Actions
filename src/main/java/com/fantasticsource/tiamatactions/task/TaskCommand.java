package com.fantasticsource.tiamatactions.task;

import com.fantasticsource.tiamatactions.gui.TaskGUI;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TaskCommand extends Task
{
    @Override
    public boolean valid()
    {
        return taskArgs.length == 1;
    }

    @Override
    public String getDescription()
    {
        return valid() ? "Run Command: " + taskArgs[0] : "Run Command";
    }

    @Override
    public boolean run(ICommandSender controller)
    {
        if (!valid()) return false;
        return FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(controller, taskArgs[0].replaceAll("@p|@P", controller.getName())) > 0;
    }

    @Override
    public TaskGUI getTaskGUI()
    {
        //TODO
        return null;
    }
}
