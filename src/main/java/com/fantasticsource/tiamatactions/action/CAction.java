package com.fantasticsource.tiamatactions.action;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.component.CInt;
import com.fantasticsource.tools.component.CStringUTF8;
import com.fantasticsource.tools.component.Component;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CAction extends Component
{
    public static final LinkedHashMap<String, CAction> ALL_ACTIONS = new LinkedHashMap<>();

    static
    {
        ALL_ACTIONS.put("None", null);

        //TODO remove test code below
        if (MCTools.devEnv())
        {
//            CAction action = new CAction("Test1");
//            CNodeCommand command = new CNodeCommand();
//            action.startTasks.add(command);
//            command.command = "/time set 1000";
        }
    }

    public String name;
    public Entity source;
    public ActionQueue queue;
    public CAction mainAction;
    public boolean valid = true, started = false;
    public final LinkedHashMap<String, ArrayList<CNode>> EVENT_TASK_LISTS = new LinkedHashMap<>();
    public final ArrayList<CNode>
            initTasks = new ArrayList<>(),
            startTasks = new ArrayList<>(),
            tickTasks = new ArrayList<>(),
            endTasks = new ArrayList<>();
    private final LinkedHashMap<String, Object> actionVars = new LinkedHashMap<>();


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

    public CAction(String name)
    {
        this();
        this.name = name;
        ALL_ACTIONS.put(name, this);
    }


    public void queue(Entity source, String queueName, CAction mainAction)
    {
        ActionQueue queue = ActionQueue.ENTITY_ACTION_QUEUES.get(source).get(queueName);

        CAction action = (CAction) copy();
        action.source = source;
        action.queue = queue;
        action.mainAction = mainAction == null ? action : mainAction;

        //"Execute immediate" style
        if ((queue == null || queue.queue.size() == 0) && action.tickTasks.size() == 0)
        {
            for (CNode task : action.initTasks)
            {
                task.execute(action);
                if (!action.valid) return;
            }

            for (CNode task : action.startTasks)
            {
                task.execute(action);
                if (!action.valid) return;
            }

            for (CNode task : action.endTasks)
            {
                task.execute(action);
                if (!action.valid) return;
            }

            return;
        }

        if (queue.size <= 0) return;
        if (queue.queue.size() >= queue.size && !queue.replaceLastIfFull) return;


        for (CNode task : action.initTasks)
        {
            task.execute(action);
            if (!action.valid) return;
        }


        if (queue.queue.size() < queue.size) queue.queue.add(action);
        else //queue.replaceLastIfFull == true, based on previous check
        {
            queue.queue.remove(queue.queue.size() - 1);
            queue.queue.add(action);
        }
    }


    @Override
    public CAction write(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, name);

        buf.writeInt(initTasks.size());
        for (CNode task : initTasks) writeMarked(buf, task);

        buf.writeInt(startTasks.size());
        for (CNode task : startTasks) writeMarked(buf, task);

        buf.writeInt(tickTasks.size());
        for (CNode task : tickTasks) writeMarked(buf, task);

        buf.writeInt(endTasks.size());
        for (CNode task : endTasks) writeMarked(buf, task);

        return this;
    }

    @Override
    public CAction read(ByteBuf buf)
    {
        name = ByteBufUtils.readUTF8String(buf);

        initTasks.clear();
        for (int i = buf.readInt(); i > 0; i--) initTasks.add((CNode) readMarked(buf));

        startTasks.clear();
        for (int i = buf.readInt(); i > 0; i--) startTasks.add((CNode) readMarked(buf));

        tickTasks.clear();
        for (int i = buf.readInt(); i > 0; i--) tickTasks.add((CNode) readMarked(buf));

        endTasks.clear();
        for (int i = buf.readInt(); i > 0; i--) endTasks.add((CNode) readMarked(buf));

        return this;
    }

    @Override
    public CAction save(OutputStream stream)
    {
        CStringUTF8 cs = new CStringUTF8().set(name).save(stream);

        CInt ci = new CInt().set(initTasks.size()).save(stream);
        for (CNode task : initTasks) saveMarked(stream, task);

        ci.set(startTasks.size()).save(stream);
        for (CNode task : startTasks) saveMarked(stream, task);

        ci.set(tickTasks.size()).save(stream);
        for (CNode task : tickTasks) saveMarked(stream, task);

        ci.set(endTasks.size()).save(stream);
        for (CNode task : endTasks) saveMarked(stream, task);

        return this;
    }

    @Override
    public CAction load(InputStream stream)
    {
        CStringUTF8 cs = new CStringUTF8().load(stream);
        name = cs.value;

        CInt ci = new CInt();
        initTasks.clear();
        for (int i = ci.load(stream).value; i > 0; i--) initTasks.add((CNode) loadMarked(stream));

        startTasks.clear();
        for (int i = ci.load(stream).value; i > 0; i--) startTasks.add((CNode) loadMarked(stream));

        tickTasks.clear();
        for (int i = ci.load(stream).value; i > 0; i--) tickTasks.add((CNode) loadMarked(stream));

        endTasks.clear();
        for (int i = ci.load(stream).value; i > 0; i--) endTasks.add((CNode) loadMarked(stream));

        return this;
    }
}
