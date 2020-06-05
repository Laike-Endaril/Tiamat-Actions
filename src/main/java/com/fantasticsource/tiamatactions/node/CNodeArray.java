package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.component.CInt;
import com.fantasticsource.tools.component.Component;
import io.netty.buffer.ByteBuf;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class CNodeArray extends CNode
{
    public ArrayList<CNode> tasks = new ArrayList<>();

    @Override
    public String getDescription()
    {
        return "Run several tasks";
    }

    @Override
    public void execute(CAction parentAction)
    {
        for (CNode task : tasks) task.execute(parentAction);
    }


    @Override
    public CNodeArray write(ByteBuf buf)
    {
        buf.writeInt(tasks.size());
        for (CNode task : tasks) Component.writeMarked(buf, task);

        return this;
    }

    @Override
    public CNodeArray read(ByteBuf buf)
    {
        tasks.clear();
        for (int i = buf.readInt(); i > 0; i--) tasks.add((CNode) Component.readMarked(buf));

        return this;
    }

    @Override
    public CNodeArray save(OutputStream stream)
    {
        new CInt().set(tasks.size()).save(stream);
        for (CNode task : tasks) Component.saveMarked(stream, task);

        return this;
    }

    @Override
    public CNodeArray load(InputStream stream)
    {
        tasks.clear();
        for (int i = new CInt().load(stream).value; i > 0; i--) tasks.add((CNode) Component.loadMarked(stream));

        return this;
    }

}
