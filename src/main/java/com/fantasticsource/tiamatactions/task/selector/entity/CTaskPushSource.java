package com.fantasticsource.tiamatactions.task.selector.entity;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.gui.TaskGUI;
import com.fantasticsource.tiamatactions.task.CTask;
import com.fantasticsource.tools.component.CBoolean;
import io.netty.buffer.ByteBuf;

import java.io.InputStream;
import java.io.OutputStream;

public class CTaskPushSource extends CTask
{
    public boolean toSubaction = false;

    @Override
    public String getDescription()
    {
        return "Store a reference to the entity this action came from";
    }

    @Override
    public void execute(CAction parentAction)
    {
        if (toSubaction) action.stack.push(action.source);
        else parentAction.stack.push(action.source);
    }

    @Override
    public TaskGUI getTaskGUI()
    {
        //TODO
        return null;
    }


    @Override
    public CTask write(ByteBuf buf)
    {
        buf.writeBoolean(toSubaction);

        return this;
    }

    @Override
    public CTask read(ByteBuf buf)
    {
        toSubaction = buf.readBoolean();

        return this;
    }

    @Override
    public CTask save(OutputStream stream)
    {
        new CBoolean().set(toSubaction).save(stream);

        return this;
    }

    @Override
    public CTask load(InputStream stream)
    {
        toSubaction = new CBoolean().load(stream).value;

        return this;
    }
}
