package com.fantasticsource.tiamatactions.task;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TaskCommand extends Task
{
    protected String command;

    public TaskCommand(String command)
    {
        this.command = command;
    }

    @Override
    public void run(ICommandSender controller, Object... args)
    {
        FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(controller, command.replaceAll("@p|@P", controller.getName()));
    }
}
