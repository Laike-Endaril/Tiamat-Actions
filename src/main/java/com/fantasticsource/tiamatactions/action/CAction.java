package com.fantasticsource.tiamatactions.action;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tiamatactions.node.CNodeCommand;
import com.fantasticsource.tiamatactions.node.staticoutput.CNodeString;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.component.CInt;
import com.fantasticsource.tools.component.CLong;
import com.fantasticsource.tools.component.CStringUTF8;
import com.fantasticsource.tools.component.Component;
import com.fantasticsource.tools.datastructures.Pair;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CAction extends Component
{
    public static final LinkedHashMap<String, CAction> ALL_ACTIONS = new LinkedHashMap<>();

    static
    {
        ALL_ACTIONS.put("None", null);

        //TODO remove test code below
        if (MCTools.devEnv())
        {
            CAction action = new CAction("Test1");
            String event = "start";

            CNodeString stringNode = new CNodeString(action.name, event, 100, 0);
            action.EVENT_NODES.get(event).put(Tools.getLong(stringNode.x, stringNode.y), stringNode);
            stringNode.string = "/time set @1";

            CNodeString stringNode2 = new CNodeString(action.name, event, 0, 100);
            action.EVENT_NODES.get(event).put(Tools.getLong(stringNode2.x, stringNode2.y), stringNode2);
            stringNode2.string = "1000";

            CNodeCommand commandNode = new CNodeCommand(action.name, event, 100, 100);
            action.EVENT_NODES.get(event).put(Tools.getLong(commandNode.x, commandNode.y), commandNode);
//            commandNode.inputNodePositions.add(Tools.getLong(stringNode.x, stringNode.y));
//            commandNode.inputNodePositions.add(Tools.getLong(stringNode2.x, stringNode2.y));

            action.startEndpointNodes.add(commandNode);


//            CAction action = new CAction("Test1");
//            String event = "start";
//
//            CNodeString stringNode = new CNodeString(action.name, event, 0, 0);
//            action.EVENT_NODES.get(event).put(Tools.getLong(stringNode.x, stringNode.y), stringNode);
//            stringNode.string = "/time set 1000";
//
//            CNodeCommand commandNode = new CNodeCommand(action.name, event, 100, 0);
//            action.EVENT_NODES.get(event).put(Tools.getLong(commandNode.x, commandNode.y), commandNode);
//            commandNode.inputNodePositions.add(Tools.getLong(stringNode.x, stringNode.y));
//
//            action.startEndpointNodes.add(commandNode);
        }
    }

    public String name;
    public Entity source;
    public ActionQueue queue;
    public CAction mainAction;
    public boolean active = true, started = false;
    public final LinkedHashMap<String, ArrayList<CNode>> EVENT_ENDPOINT_NODES = new LinkedHashMap<>();
    public final LinkedHashMap<String, LinkedHashMap<Long, CNode>> EVENT_NODES = new LinkedHashMap<>();
    public final ArrayList<CNode>
            initEndpointNodes = new ArrayList<>(),
            startEndpointNodes = new ArrayList<>(),
            tickEndpointNodes = new ArrayList<>(),
            endEndpointNodes = new ArrayList<>();
    public final LinkedHashMap<Long, CNode>
            initNodes = new LinkedHashMap<>(),
            startNodes = new LinkedHashMap<>(),
            tickNodes = new LinkedHashMap<>(),
            endNodes = new LinkedHashMap<>();
    public final LinkedHashMap<String, Object> actionVars = new LinkedHashMap<>();


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
        if ((queue == null || queue.queue.size() == 0) && action.tickEndpointNodes.size() == 0)
        {
            action.execute("init");
            action.execute("start");
            action.execute("end");
            return;
        }

        if (queue.size <= 0) return;
        if (queue.queue.size() >= queue.size && !queue.replaceLastIfFull) return;


        action.execute("init");
        if (!action.active) return;


        if (queue.queue.size() < queue.size) queue.queue.add(action);
        else //queue.replaceLastIfFull == true, based on previous check
        {
            queue.queue.remove(queue.queue.size() - 1);
            queue.queue.add(action);
        }
    }

    protected void execute(String event)
    {
        HashMap<Long, Object> results = new HashMap<>();
        switch (event)
        {
            case "init":
                for (CNode endNode : initEndpointNodes) endNode.executeTree(this, results);
                break;

            case "start":
                for (CNode endNode : startEndpointNodes) endNode.executeTree(this, results);
                break;

            case "tick":
                for (CNode endNode : tickEndpointNodes) endNode.executeTree(this, results);
                break;

            case "end":
                for (CNode endNode : endEndpointNodes) endNode.executeTree(this, results);
                break;
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
            writeMarked(buf, entry.getValue());
        }

        buf.writeInt(startNodes.size());
        for (Map.Entry<Long, CNode> entry : startNodes.entrySet())
        {
            buf.writeLong(entry.getKey());
            writeMarked(buf, entry.getValue());
        }

        buf.writeInt(tickNodes.size());
        for (Map.Entry<Long, CNode> entry : tickNodes.entrySet())
        {
            buf.writeLong(entry.getKey());
            writeMarked(buf, entry.getValue());
        }

        buf.writeInt(endNodes.size());
        for (Map.Entry<Long, CNode> entry : endNodes.entrySet())
        {
            buf.writeLong(entry.getKey());
            writeMarked(buf, entry.getValue());
        }


        buf.writeInt(initEndpointNodes.size());
        for (CNode task : initEndpointNodes) writeMarked(buf, task);

        buf.writeInt(startEndpointNodes.size());
        for (CNode task : startEndpointNodes) writeMarked(buf, task);

        buf.writeInt(tickEndpointNodes.size());
        for (CNode task : tickEndpointNodes) writeMarked(buf, task);

        buf.writeInt(endEndpointNodes.size());
        for (CNode task : endEndpointNodes) writeMarked(buf, task);


        return this;
    }

    @Override
    public CAction read(ByteBuf buf)
    {
        name = ByteBufUtils.readUTF8String(buf);


        initNodes.clear();
        for (int i = buf.readInt(); i > 0; i--) initNodes.put(Tools.getLong(buf.readInt(), buf.readInt()), (CNode) readMarked(buf));

        startNodes.clear();
        for (int i = buf.readInt(); i > 0; i--) startNodes.put(Tools.getLong(buf.readInt(), buf.readInt()), (CNode) readMarked(buf));

        tickNodes.clear();
        for (int i = buf.readInt(); i > 0; i--) tickNodes.put(Tools.getLong(buf.readInt(), buf.readInt()), (CNode) readMarked(buf));

        endNodes.clear();
        for (int i = buf.readInt(); i > 0; i--) endNodes.put(Tools.getLong(buf.readInt(), buf.readInt()), (CNode) readMarked(buf));


        initEndpointNodes.clear();
        for (int i = buf.readInt(); i > 0; i--) initEndpointNodes.add((CNode) readMarked(buf));

        startEndpointNodes.clear();
        for (int i = buf.readInt(); i > 0; i--) startEndpointNodes.add((CNode) readMarked(buf));

        tickEndpointNodes.clear();
        for (int i = buf.readInt(); i > 0; i--) tickEndpointNodes.add((CNode) readMarked(buf));

        endEndpointNodes.clear();
        for (int i = buf.readInt(); i > 0; i--) endEndpointNodes.add((CNode) readMarked(buf));


        for (CNode node : initEndpointNodes) initNodes.put(Tools.getLong(node.x, node.y), node);
        for (CNode node : startEndpointNodes) startNodes.put(Tools.getLong(node.x, node.y), node);
        for (CNode node : tickEndpointNodes) tickNodes.put(Tools.getLong(node.x, node.y), node);
        for (CNode node : endEndpointNodes) endNodes.put(Tools.getLong(node.x, node.y), node);


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
            saveMarked(stream, entry.getValue());
        }

        ci.set(startNodes.size()).save(stream);
        for (Map.Entry<Long, CNode> entry : startNodes.entrySet())
        {
            cl.set(entry.getKey()).save(stream);
            saveMarked(stream, entry.getValue());
        }

        ci.set(tickNodes.size()).save(stream);
        for (Map.Entry<Long, CNode> entry : tickNodes.entrySet())
        {
            cl.set(entry.getKey()).save(stream);
            saveMarked(stream, entry.getValue());
        }

        ci.set(endNodes.size()).save(stream);
        for (Map.Entry<Long, CNode> entry : endNodes.entrySet())
        {
            cl.set(entry.getKey()).save(stream);
            saveMarked(stream, entry.getValue());
        }


        ci.set(initEndpointNodes.size()).save(stream);
        for (CNode task : initEndpointNodes) saveMarked(stream, task);

        ci.set(startEndpointNodes.size()).save(stream);
        for (CNode task : startEndpointNodes) saveMarked(stream, task);

        ci.set(tickEndpointNodes.size()).save(stream);
        for (CNode task : tickEndpointNodes) saveMarked(stream, task);

        ci.set(endEndpointNodes.size()).save(stream);
        for (CNode task : endEndpointNodes) saveMarked(stream, task);


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
        for (int i = ci.load(stream).value; i > 0; i--) initNodes.put(cl.load(stream).value, (CNode) loadMarked(stream));

        startNodes.clear();
        for (int i = ci.load(stream).value; i > 0; i--) startNodes.put(cl.load(stream).value, (CNode) loadMarked(stream));

        tickNodes.clear();
        for (int i = ci.load(stream).value; i > 0; i--) tickNodes.put(cl.load(stream).value, (CNode) loadMarked(stream));

        endNodes.clear();
        for (int i = ci.load(stream).value; i > 0; i--) endNodes.put(cl.load(stream).value, (CNode) loadMarked(stream));


        initEndpointNodes.clear();
        for (int i = ci.load(stream).value; i > 0; i--) initEndpointNodes.add((CNode) loadMarked(stream));

        startEndpointNodes.clear();
        for (int i = ci.load(stream).value; i > 0; i--) startEndpointNodes.add((CNode) loadMarked(stream));

        tickEndpointNodes.clear();
        for (int i = ci.load(stream).value; i > 0; i--) tickEndpointNodes.add((CNode) loadMarked(stream));

        endEndpointNodes.clear();
        for (int i = ci.load(stream).value; i > 0; i--) endEndpointNodes.add((CNode) loadMarked(stream));


        for (CNode node : initEndpointNodes) initNodes.put(Tools.getLong(node.x, node.y), node);
        for (CNode node : startEndpointNodes) startNodes.put(Tools.getLong(node.x, node.y), node);
        for (CNode node : tickEndpointNodes) tickNodes.put(Tools.getLong(node.x, node.y), node);
        for (CNode node : endEndpointNodes) endNodes.put(Tools.getLong(node.x, node.y), node);


        return this;
    }
}
