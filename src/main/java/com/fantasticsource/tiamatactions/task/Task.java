package com.fantasticsource.tiamatactions.task;

import net.minecraft.command.ICommandSender;

public abstract class Task
{
    public abstract void run(ICommandSender controller, Object... args);
}
