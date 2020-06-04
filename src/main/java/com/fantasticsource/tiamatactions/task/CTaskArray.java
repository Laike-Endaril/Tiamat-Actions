package com.fantasticsource.tiamatactions.task;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.gui.TaskGUI;
import com.fantasticsource.tools.component.CInt;
import com.fantasticsource.tools.component.Component;
import io.netty.buffer.ByteBuf;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class CTaskArray extends CTask
{
    private ArrayList<CTask> tasks = new ArrayList<>();

    @Override
    public String getDescription()
    {
        return "Run several tasks";
    }

    @Override
    public void execute(CAction parentAction)
    {
        for (CTask task : tasks) task.execute(parentAction);
    }

    @Override
    public TaskGUI getTaskGUI()
    {
        //TODO
        return null;
    }


    @Override
    public CTaskArray write(ByteBuf buf)
    {
        buf.writeInt(tasks.size());
        for (CTask task : tasks) Component.writeMarked(buf, task);

        return this;
    }

    @Override
    public CTaskArray read(ByteBuf buf)
    {
        tasks.clear();
        for (int i = buf.readInt(); i > 0; i--) tasks.add((CTask) Component.readMarked(buf));

        return this;
    }

    @Override
    public CTaskArray save(OutputStream stream)
    {
        new CInt().set(tasks.size()).save(stream);
        for (CTask task : tasks) Component.saveMarked(stream, task);

        return this;
    }

    @Override
    public CTaskArray load(InputStream stream)
    {
        tasks.clear();
        for (int i = new CInt().load(stream).value; i > 0; i--) tasks.add((CTask) Component.loadMarked(stream));

        return this;
    }

}
