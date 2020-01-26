package com.fantasticsource.tiamatactions.task;

import com.fantasticsource.tiamatactions.action.Action;
import com.fantasticsource.tiamatactions.action.ActionTaskHandler;
import com.fantasticsource.tiamatactions.gui.TaskGUI;
import com.fantasticsource.tools.component.Component;
import io.netty.buffer.ByteBuf;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public abstract class CTask extends Component
{
    public final Action action;
    public ArrayList<CTask> nextTasks = new ArrayList<>();
    public boolean stopOnActionInterrupt = true;


    public CTask(Action action)
    {
        this.action = action;
    }


    public abstract String getDescription();


    public abstract void tick(ActionTaskHandler handler);


    public abstract TaskGUI getTaskGUI();


    @Override
    public Component write(ByteBuf byteBuf)
    {
        //TODO
        return null;
    }

    @Override
    public Component read(ByteBuf byteBuf)
    {
        //TODO
        return null;
    }

    @Override
    public Component save(OutputStream outputStream)
    {
        //TODO
        return null;
    }

    @Override
    public Component load(InputStream inputStream)
    {
        //TODO
        return null;
    }
}
