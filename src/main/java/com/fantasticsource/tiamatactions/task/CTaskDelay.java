package com.fantasticsource.tiamatactions.task;

import com.fantasticsource.tiamatactions.action.ActionTaskHandler;
import com.fantasticsource.tiamatactions.gui.TaskGUI;
import com.fantasticsource.tools.component.CInt;
import io.netty.buffer.ByteBuf;

import java.io.InputStream;
import java.io.OutputStream;

public class CTaskDelay extends CTask
{
    public int delay = 20, elapsed = 0;


    @Override
    public String getDescription()
    {
        return "Delay for " + delay + " ticks";
    }

    @Override
    public void tick(ActionTaskHandler handler)
    {
        if (elapsed++ == delay) handler.currentTasks.addAll(nextTasks);
        else handler.queuedTasks.add(this);
    }

    @Override
    public TaskGUI getTaskGUI()
    {
        //TODO
        return null;
    }


    @Override
    public CTaskDelay write(ByteBuf buf)
    {
        buf.writeInt(delay);

        super.write(buf);

        return this;
    }

    @Override
    public CTaskDelay read(ByteBuf buf)
    {
        delay = buf.readInt();
        elapsed = 0;

        super.read(buf);

        return this;
    }

    @Override
    public CTaskDelay save(OutputStream stream)
    {
        new CInt().set(delay).save(stream);

        super.save(stream);

        return this;
    }

    @Override
    public CTaskDelay load(InputStream stream)
    {
        delay = new CInt().load(stream).value;
        elapsed = 0;

        super.load(stream);

        return this;
    }
}
