package com.fantasticsource.tiamatactions.task;

import com.fantasticsource.mctools.gui.element.text.filter.FilterBoolean;
import com.fantasticsource.tiamatactions.gui.TaskGUI;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;

public abstract class Task
{
    public static final int GLOBAL_ARGUMENT_COUNT = 1;
    protected boolean stopOnFailure = false;
    protected String[] taskArgs = new String[0];


    public final boolean getStopOnFailure()
    {
        return stopOnFailure;
    }

    public final void setStopOnFailure(boolean stopOnFailure)
    {
        this.stopOnFailure = stopOnFailure;
    }


    public final void setAllArgs(String... taskArgs)
    {
        stopOnFailure = FilterBoolean.INSTANCE.parse(taskArgs[0]);

        String[] args = new String[taskArgs.length - GLOBAL_ARGUMENT_COUNT];
        System.arraycopy(taskArgs, 1, args, 0, args.length);
        setArgs(args);
    }

    public final void setArgs(String... taskArgs)
    {
        this.taskArgs = taskArgs;
    }


    public abstract boolean valid();


    public abstract String getDescription();


    public abstract boolean run(ICommandSender controller, @Nullable ItemStack activatingItem, LinkedHashMap<String, Object> vars);


    public abstract TaskGUI getTaskGUI();
}
