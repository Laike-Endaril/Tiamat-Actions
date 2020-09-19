package com.fantasticsource.tiamatactions.action;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.component.CInt;
import com.fantasticsource.tools.component.CLong;
import com.fantasticsource.tools.component.CStringUTF8;
import com.fantasticsource.tools.component.Component;
import com.fantasticsource.tools.datastructures.ExplicitPriorityQueue;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CAction extends Component
{
    public static final String DIR_PREFIX = MCTools.getConfigDir() + MODID + File.separator + "actions";
    public static final LinkedHashMap<String, CAction> ALL_ACTIONS = new LinkedHashMap<>();

    static
    {
        ALL_ACTIONS.put("None", null);
    }

    public String name;
    public Entity source;
    public ActionQueue queue;
    public CAction mainAction;
    public boolean active = true, started = false;
    public final LinkedHashMap<String, ExplicitPriorityQueue<CNode>> EVENT_ENDPOINT_NODES = new LinkedHashMap<>();
    public final LinkedHashMap<String, LinkedHashMap<Long, CNode>> EVENT_NODES = new LinkedHashMap<>();
    public final ExplicitPriorityQueue<CNode>
            initEndpointNodes = new ExplicitPriorityQueue<>(),
            startEndpointNodes = new ExplicitPriorityQueue<>(),
            tickEndpointNodes = new ExplicitPriorityQueue<>(),
            endEndpointNodes = new ExplicitPriorityQueue<>();
    public final LinkedHashMap<Long, CNode>
            initNodes = new LinkedHashMap<>(),
            startNodes = new LinkedHashMap<>(),
            tickNodes = new LinkedHashMap<>(),
            endNodes = new LinkedHashMap<>();
    public LinkedHashMap<String, Object> actionVars = new LinkedHashMap<>();
    public Object argument = null, result = null;

    public HashSet<StackTraceElement[]> loggedErrors = new HashSet<>();


    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CAction()
    {
        EVENT_ENDPOINT_NODES.put("init", initEndpointNodes);
        EVENT_ENDPOINT_NODES.put("start", startEndpointNodes);
        EVENT_ENDPOINT_NODES.put("tick", tickEndpointNodes);
        EVENT_ENDPOINT_NODES.put("end", endEndpointNodes);

        EVENT_NODES.put("init", initNodes);
        EVENT_NODES.put("start", startNodes);
        EVENT_NODES.put("tick", tickNodes);
        EVENT_NODES.put("end", endNodes);
    }

    public CAction(String name)
    {
        this();
        this.name = name;
    }


    public Object queue(Entity source, String queueName)
    {
        return queue(source, queueName, null);
    }

    public Object queue(Entity source, String queueName, CAction mainAction)
    {
        return queue(source, queueName, mainAction, null);
    }

    public Object queue(Entity source, String queueName, CAction mainAction, Object argument)
    {
        return queue(source, queueName, mainAction, argument, null);
    }

    public Object queue(Entity source, String queueName, CAction mainAction, Object argument, LinkedHashMap<String, Object> actionVars)
    {
        ActionQueue queue = queueName == null ? null : ActionQueue.get(source, queueName);

        CAction action = (CAction) copy();
        action.source = source;
        action.queue = queue;
        action.mainAction = mainAction == null ? action : mainAction;
        action.argument = argument;
        if (actionVars != null) action.actionVars = actionVars;

        //"Execute immediate" style
        if ((queue == null || queue.queue.size() == 0) && action.tickEndpointNodes.size() == 0)
        {
            action.execute(source, "init");
            action.execute(source, "start");
            action.execute(source, "end");
            return action.result;
        }

        if (queue.size <= 0) return action.result;
        if (queue.queue.size() >= queue.size && !queue.replaceLastIfFull) return action.result;


        action.execute(source, "init");
        if (!action.mainAction.active) return action.result;


        if (queue.queue.size() < queue.size) queue.queue.add(action);
        else //queue.replaceLastIfFull == true, based on previous check
        {
            queue.queue.remove(queue.queue.size() - 1);
            queue.queue.add(action);
        }

        return action.result;
    }

    protected void execute(Entity source, String event)
    {
        Profiler profiler = source.world.profiler;
        profiler.startSection("Tiamat Action: " + name);
        profiler.startSection(event);

        HashMap<Long, Object> results = new HashMap<>();
        switch (event)
        {
            case "init":
                for (CNode endNode : initEndpointNodes.toArray(new CNode[0])) endNode.executeTree(mainAction, this, results);
                break;

            case "start":
                for (CNode endNode : startEndpointNodes.toArray(new CNode[0])) endNode.executeTree(mainAction, this, results);
                break;

            case "tick":
                for (CNode endNode : tickEndpointNodes.toArray(new CNode[0])) endNode.executeTree(mainAction, this, results);
                break;

            case "end":
                for (CNode endNode : endEndpointNodes.toArray(new CNode[0])) endNode.executeTree(mainAction, this, results, true);
                break;
        }

        profiler.endSection();
        profiler.endSection();
    }


    public void save()
    {
        CAction.ALL_ACTIONS.put(name, this);


        File file = new File(DIR_PREFIX + File.separator + name + ".dat");
        file.mkdirs();
        while (file.exists()) file.delete();

        try
        {
            FileOutputStream stream = new FileOutputStream(file);
            save(stream);
            stream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void loadAll()
    {
        File dir = new File(DIR_PREFIX + File.separator);
        dir.mkdirs();

        for (String filename : Tools.allRecursiveRelativeFilenames(dir.getAbsolutePath()))
        {
            File file = new File(dir.getAbsolutePath() + File.separator + filename);
            try
            {
                FileInputStream stream = new FileInputStream(file);
                CAction action = new CAction().load(stream);
                action.name = filename.replace(".dat", "").replaceAll(Pattern.quote("\\"), "/");
                stream.close();
                ALL_ACTIONS.put(action.name, action);
            }
            catch (Exception e)
            {
                System.err.println(TextFormatting.RED + "Error loading action: " + file.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }

    public void delete()
    {
        ALL_ACTIONS.remove(name);

        File file = new File(DIR_PREFIX + File.separator + name + ".dat");
        while (file.exists()) file.delete();

        String dir = DIR_PREFIX + File.separator + name + File.separator + "..";
        file = new File(dir);
        File[] files = file.listFiles();
        while (files != null && files.length == 0)
        {
            while (file.exists()) file.delete();
            dir += File.separator + "..";
            file = new File(dir);
            files = file.listFiles();
        }
    }


    @Override
    public CAction write(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, name);


        buf.writeInt(initNodes.size());
        for (Map.Entry<Long, CNode> entry : initNodes.entrySet())
        {
            buf.writeLong(entry.getKey());
            entry.getValue().actionName = name;
            writeMarked(buf, entry.getValue());
        }

        buf.writeInt(startNodes.size());
        for (Map.Entry<Long, CNode> entry : startNodes.entrySet())
        {
            buf.writeLong(entry.getKey());
            entry.getValue().actionName = name;
            writeMarked(buf, entry.getValue());
        }

        buf.writeInt(tickNodes.size());
        for (Map.Entry<Long, CNode> entry : tickNodes.entrySet())
        {
            buf.writeLong(entry.getKey());
            entry.getValue().actionName = name;
            writeMarked(buf, entry.getValue());
        }

        buf.writeInt(endNodes.size());
        for (Map.Entry<Long, CNode> entry : endNodes.entrySet())
        {
            buf.writeLong(entry.getKey());
            entry.getValue().actionName = name;
            writeMarked(buf, entry.getValue());
        }


        buf.writeInt(initEndpointNodes.size());
        for (CNode endPointNode : initEndpointNodes.toArray(new CNode[0]))
        {
            buf.writeLong(Tools.getLong(endPointNode.y, endPointNode.x));
        }

        buf.writeInt(startEndpointNodes.size());
        for (CNode endPointNode : startEndpointNodes.toArray(new CNode[0]))
        {
            buf.writeLong(Tools.getLong(endPointNode.y, endPointNode.x));
        }

        buf.writeInt(tickEndpointNodes.size());
        for (CNode endPointNode : tickEndpointNodes.toArray(new CNode[0]))
        {
            buf.writeLong(Tools.getLong(endPointNode.y, endPointNode.x));
        }

        buf.writeInt(endEndpointNodes.size());
        for (CNode endPointNode : endEndpointNodes.toArray(new CNode[0]))
        {
            buf.writeLong(Tools.getLong(endPointNode.y, endPointNode.x));
        }


        return this;
    }

    @Override
    public CAction read(ByteBuf buf)
    {
        name = ByteBufUtils.readUTF8String(buf);


        initNodes.clear();
        for (int i = buf.readInt(); i > 0; i--)
        {
            long pos = buf.readLong();
            CNode node = (CNode) readMarked(buf);
            node.actionName = name;
            initNodes.put(pos, node);
        }

        startNodes.clear();
        for (int i = buf.readInt(); i > 0; i--)
        {
            long pos = buf.readLong();
            CNode node = (CNode) readMarked(buf);
            node.actionName = name;
            startNodes.put(pos, node);
        }

        tickNodes.clear();
        for (int i = buf.readInt(); i > 0; i--)
        {
            long pos = buf.readLong();
            CNode node = (CNode) readMarked(buf);
            node.actionName = name;
            tickNodes.put(pos, node);
        }

        endNodes.clear();
        for (int i = buf.readInt(); i > 0; i--)
        {
            long pos = buf.readLong();
            CNode node = (CNode) readMarked(buf);
            node.actionName = name;
            endNodes.put(pos, node);
        }


        initEndpointNodes.clear();
        for (int i = buf.readInt(); i > 0; i--)
        {
            long pos = buf.readLong();
            initEndpointNodes.add(initNodes.get(pos), pos);
        }

        startEndpointNodes.clear();
        for (int i = buf.readInt(); i > 0; i--)
        {
            long pos = buf.readLong();
            startEndpointNodes.add(startNodes.get(pos), pos);
        }

        tickEndpointNodes.clear();
        for (int i = buf.readInt(); i > 0; i--)
        {
            long pos = buf.readLong();
            tickEndpointNodes.add(tickNodes.get(pos), pos);
        }

        endEndpointNodes.clear();
        for (int i = buf.readInt(); i > 0; i--)
        {
            long pos = buf.readLong();
            endEndpointNodes.add(endNodes.get(pos), pos);
        }


        return this;
    }

    @Override
    public CAction save(OutputStream stream)
    {
        new CStringUTF8().set(name).save(stream);


        CInt ci = new CInt().set(initNodes.size()).save(stream);
        CLong cl = new CLong();
        for (Map.Entry<Long, CNode> entry : initNodes.entrySet())
        {
            cl.set(entry.getKey()).save(stream);
            entry.getValue().actionName = name;
            saveMarked(stream, entry.getValue());
        }

        ci.set(startNodes.size()).save(stream);
        for (Map.Entry<Long, CNode> entry : startNodes.entrySet())
        {
            cl.set(entry.getKey()).save(stream);
            entry.getValue().actionName = name;
            saveMarked(stream, entry.getValue());
        }

        ci.set(tickNodes.size()).save(stream);
        for (Map.Entry<Long, CNode> entry : tickNodes.entrySet())
        {
            cl.set(entry.getKey()).save(stream);
            entry.getValue().actionName = name;
            saveMarked(stream, entry.getValue());
        }

        ci.set(endNodes.size()).save(stream);
        for (Map.Entry<Long, CNode> entry : endNodes.entrySet())
        {
            cl.set(entry.getKey()).save(stream);
            entry.getValue().actionName = name;
            saveMarked(stream, entry.getValue());
        }


        ci.set(initEndpointNodes.size()).save(stream);
        for (CNode endPointNode : initEndpointNodes.toArray(new CNode[0]))
        {
            cl.set(Tools.getLong(endPointNode.y, endPointNode.x)).save(stream);
        }

        ci.set(startEndpointNodes.size()).save(stream);
        for (CNode endPointNode : startEndpointNodes.toArray(new CNode[0]))
        {
            cl.set(Tools.getLong(endPointNode.y, endPointNode.x)).save(stream);
        }

        ci.set(tickEndpointNodes.size()).save(stream);
        for (CNode endPointNode : tickEndpointNodes.toArray(new CNode[0]))
        {
            cl.set(Tools.getLong(endPointNode.y, endPointNode.x)).save(stream);
        }

        ci.set(endEndpointNodes.size()).save(stream);
        for (CNode endPointNode : endEndpointNodes.toArray(new CNode[0]))
        {
            cl.set(Tools.getLong(endPointNode.y, endPointNode.x)).save(stream);
        }


        return this;
    }

    @Override
    public CAction load(InputStream stream)
    {
        CStringUTF8 cs = new CStringUTF8().load(stream);
        name = cs.value;


        CInt ci = new CInt();
        CLong cl = new CLong();
        initNodes.clear();
        for (int i = ci.load(stream).value; i > 0; i--)
        {
            long pos = cl.load(stream).value;
            CNode node = (CNode) loadMarked(stream);
            node.actionName = name;
            initNodes.put(pos, node);
        }

        startNodes.clear();
        for (int i = ci.load(stream).value; i > 0; i--)
        {
            long pos = cl.load(stream).value;
            CNode node = (CNode) loadMarked(stream);
            node.actionName = name;
            startNodes.put(pos, node);
        }

        tickNodes.clear();
        for (int i = ci.load(stream).value; i > 0; i--)
        {
            long pos = cl.load(stream).value;
            CNode node = (CNode) loadMarked(stream);
            node.actionName = name;
            tickNodes.put(pos, node);
        }

        endNodes.clear();
        for (int i = ci.load(stream).value; i > 0; i--)
        {
            long pos = cl.load(stream).value;
            CNode node = (CNode) loadMarked(stream);
            node.actionName = name;
            endNodes.put(pos, node);
        }


        initEndpointNodes.clear();
        for (int i = ci.load(stream).value; i > 0; i--)
        {
            long pos = cl.load(stream).value;
            initEndpointNodes.add(initNodes.get(pos), pos);
        }

        startEndpointNodes.clear();
        for (int i = ci.load(stream).value; i > 0; i--)
        {
            long pos = cl.load(stream).value;
            startEndpointNodes.add(startNodes.get(pos), pos);
        }

        tickEndpointNodes.clear();
        for (int i = ci.load(stream).value; i > 0; i--)
        {
            long pos = cl.load(stream).value;
            tickEndpointNodes.add(tickNodes.get(pos), pos);
        }

        endEndpointNodes.clear();
        for (int i = ci.load(stream).value; i > 0; i--)
        {
            long pos = cl.load(stream).value;
            endEndpointNodes.add(endNodes.get(pos), pos);
        }


        return this;
    }
}
