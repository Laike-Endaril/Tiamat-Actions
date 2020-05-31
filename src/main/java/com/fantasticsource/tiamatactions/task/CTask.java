package com.fantasticsource.tiamatactions.task;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.gui.TaskGUI;
import com.fantasticsource.tools.component.Component;
import io.netty.buffer.ByteBuf;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public abstract class CTask extends Component
{
    public CAction action;
    private ArrayList<CTask> nextTasks = new ArrayList<>();


    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CTask()
    {
    }

    public CTask(CAction action)
    {
        this.action = action;
    }

    public abstract String getDescription();


    public abstract void execute(CAction action);


    public abstract TaskGUI getTaskGUI();


    public CTask queueTask(CTask task)
    {
        nextTasks.add(task);
        return this;
    }


    @Override
    public CTask write(ByteBuf buf)
    {
        return this;
    }

    @Override
    public CTask read(ByteBuf buf)
    {
        return this;
    }

    @Override
    public CTask save(OutputStream stream)
    {
        return this;
    }

    @Override
    public CTask load(InputStream stream)
    {
        return this;
    }
}
