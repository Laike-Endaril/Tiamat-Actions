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
    public static final LinkedHashMap<String, CAction> ALL_ACTIONS = new LinkedHashMap<>();

    static
    {
        ALL_ACTIONS.put("None", null);
    }

    public String name;
    public final LinkedHashMap<String, ArrayList<CTask>> EVENT_TASK_LISTS = new LinkedHashMap<>();
    public final ArrayList<CTask>
            initTasks = new ArrayList<>(),
            startTasks = new ArrayList<>(),
            tickTasks = new ArrayList<>(),
            endTasks = new ArrayList<>();
    public final LinkedHashMap<String, Component> actionVars = new LinkedHashMap<>();


    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CAction()
    {
        EVENT_TASK_LISTS.put("init", initTasks);
        EVENT_TASK_LISTS.put("start", startTasks);
        EVENT_TASK_LISTS.put("tick", tickTasks);
        EVENT_TASK_LISTS.put("end", endTasks);
    }

    protected CAction(String name)
    {
        this();
        this.name = name;
        ALL_ACTIONS.put(name, this);
    }


    public static CAction getInstance(String name)
    {
        if (name == null || name.equals("") || name.equals("None")) throw new IllegalArgumentException("Action name must not be null, empty, or default (None)!");

        return new CAction(name);
    }


    public CTask getTaskChain(String event)
    {
        return getTaskChain(event, 0);
    }

    public CTask getTaskChain(String event, int index)
    {
        return getTaskChain(event, index, Integer.MAX_VALUE);
    }

    public CTask getTaskChain(String event, int index, int maxCount)
    {
        ArrayList<CTask> tasks = EVENT_TASK_LISTS.get(event);
        if (tasks == null) return null;


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

        buf.writeInt(initTasks.size());
        for (CTask task : initTasks) writeMarked(buf, task);

        buf.writeInt(startTasks.size());
        for (CTask task : startTasks) writeMarked(buf, task);

        buf.writeInt(tickTasks.size());
        for (CTask task : tickTasks) writeMarked(buf, task);

        buf.writeInt(endTasks.size());
        for (CTask task : endTasks) writeMarked(buf, task);

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

        initTasks.clear();
        for (int i = buf.readInt(); i > 0; i--) initTasks.add((CTask) readMarked(buf));

        startTasks.clear();
        for (int i = buf.readInt(); i > 0; i--) startTasks.add((CTask) readMarked(buf));

        tickTasks.clear();
        for (int i = buf.readInt(); i > 0; i--) tickTasks.add((CTask) readMarked(buf));

        endTasks.clear();
        for (int i = buf.readInt(); i > 0; i--) endTasks.add((CTask) readMarked(buf));

        actionVars.clear();
        for (int i = buf.readInt(); i > 0; i--) actionVars.put(ByteBufUtils.readUTF8String(buf), readMarked(buf));

        return this;
    }

    @Override
    public CAction save(OutputStream stream)
    {
        CStringUTF8 cs = new CStringUTF8().set(name).save(stream);

        CInt ci = new CInt().set(initTasks.size()).save(stream);
        for (CTask task : initTasks) saveMarked(stream, task);

        ci.set(startTasks.size()).save(stream);
        for (CTask task : startTasks) saveMarked(stream, task);

        ci.set(tickTasks.size()).save(stream);
        for (CTask task : tickTasks) saveMarked(stream, task);

        ci.set(endTasks.size()).save(stream);
        for (CTask task : endTasks) saveMarked(stream, task);

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

        CInt ci = new CInt();
        initTasks.clear();
        for (int i = ci.load(stream).value; i > 0; i--) initTasks.add((CTask) loadMarked(stream));

        startTasks.clear();
        for (int i = ci.load(stream).value; i > 0; i--) startTasks.add((CTask) loadMarked(stream));

        tickTasks.clear();
        for (int i = ci.load(stream).value; i > 0; i--) tickTasks.add((CTask) loadMarked(stream));

        endTasks.clear();
        for (int i = ci.load(stream).value; i > 0; i--) endTasks.add((CTask) loadMarked(stream));

        actionVars.clear();
        for (int i = ci.load(stream).value; i > 0; i--) actionVars.put(cs.load(stream).value, loadMarked(stream));

        return this;
    }
}
