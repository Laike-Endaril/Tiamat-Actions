package com.fantasticsource.tiamatactions.action;

import com.fantasticsource.tiamatactions.task.CTask;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.component.CInt;
import com.fantasticsource.tools.component.CStringUTF8;
import com.fantasticsource.tools.component.Component;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class CAction extends Component
{
    public static LinkedHashMap<String, CAction> allActions = new LinkedHashMap<>();

    static
    {
        allActions.put("None", null);
    }

    public String name;
    public final ArrayList<CTask> tasks = new ArrayList<>();
    public final LinkedHashMap<String, Component> actionVars = new LinkedHashMap<>();

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CAction()
    {
    }

    protected CAction(String name)
    {
        this.name = name;
        allActions.put(name, this);
    }


    public static CAction getInstance(String name)
    {
        if (name == null || name.equals("") || name.equals("None")) throw new IllegalArgumentException("Action name must not be null, empty, or default (None)!");

        return new CAction(name);
    }


    public CTask getTaskChain(int index)
    {
        return getTaskChain(index, Integer.MAX_VALUE);
    }

    public CTask getTaskChain(int index, int maxCount)
    {
        CTask task = null, nextTask;

        for (int i = Tools.min(tasks.size() - 1, index + maxCount - 1); i >= index && i >= 0; i--)
        {
            nextTask = task;
            task = (CTask) tasks.get(i).copy();
            if (nextTask != null) task.queueTask(nextTask);
            else task.setRanFromAction(this);
        }

        return task;
    }


    @Override
    public CAction write(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, name);

        buf.writeInt(tasks.size());
        for (CTask task : tasks) writeMarked(buf, task);

        buf.writeInt(actionVars.size());
        for (Map.Entry<String, Component> entry : actionVars.entrySet())
        {
            ByteBufUtils.writeUTF8String(buf, entry.getKey());
            writeMarked(buf, entry.getValue());
        }

        return this;
    }

    @Override
    public CAction read(ByteBuf buf)
    {
        name = ByteBufUtils.readUTF8String(buf);

        tasks.clear();
        for (int i = buf.readInt(); i > 0; i--) tasks.add((CTask) readMarked(buf));

        actionVars.clear();
        for (int i = buf.readInt(); i > 0; i--) actionVars.put(ByteBufUtils.readUTF8String(buf), readMarked(buf));

        return this;
    }

    @Override
    public CAction save(OutputStream stream)
    {
        CStringUTF8 cs = new CStringUTF8().set(name).save(stream);

        CInt ci = new CInt().set(tasks.size()).save(stream);
        for (CTask task : tasks) saveMarked(stream, task);

        ci.set(actionVars.size()).save(stream);
        for (Map.Entry<String, Component> entry : actionVars.entrySet())
        {
            cs.set(entry.getKey()).save(stream);
            saveMarked(stream, entry.getValue());
        }

        return this;
    }

    @Override
    public CAction load(InputStream stream)
    {
        CStringUTF8 cs = new CStringUTF8().load(stream);
        name = cs.value;

        CInt ci = new CInt().load(stream);
        tasks.clear();
        for (int i = ci.load(stream).value; i > 0; i--) tasks.add((CTask) loadMarked(stream));

        actionVars.clear();
        for (int i = ci.load(stream).value; i > 0; i--) actionVars.put(cs.load(stream).value, loadMarked(stream));

        return this;
    }
}
