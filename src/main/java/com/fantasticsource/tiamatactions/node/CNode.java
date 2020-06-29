package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.gui.actioneditor.EventEditorGUI;
import com.fantasticsource.tiamatactions.gui.actioneditor.GUINode;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.component.CInt;
import com.fantasticsource.tools.component.CLong;
import com.fantasticsource.tools.component.CStringUTF8;
import com.fantasticsource.tools.component.Component;
import com.fantasticsource.tools.datastructures.ExplicitPriorityQueue;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class CNode extends Component
{
    public String actionName, eventName;
    public int x, y;

    public ArrayList<Long> inputNodePositions = new ArrayList<>();
    public ArrayList<Long> outputNodePositions = new ArrayList<>();


    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNode()
    {
    }

    public CNode(String actionName, String eventName, int x, int y)
    {
        this.actionName = actionName;
        this.eventName = eventName;
        this.x = x;
        this.y = y;
    }


    public abstract ResourceLocation getTexture();

    public abstract String getDescription();


    public abstract Class[] requiredInputTypes();

    public abstract Class arrayInputType();

    public abstract Class outputType();


    //Passing an action here because it needs to be usable from client, which doesn't have the action database
    public final String tryAddInput(CAction action, CNode inputNode)
    {
        if (inputNode == this) return "Same node";

        if (requiredInputTypes().length == 0 && arrayInputType() == null) return "This node cannot accept inputs";

        Class inputType = inputNode.outputType();
        if (inputType == null) return "Input has no output type";

        if (!Tools.areRelated(inputType, arrayInputType()))
        {
            boolean found = false;
            for (Class c : requiredInputTypes())
            {
                if (Tools.areRelated(c, inputType))
                {
                    found = true;
                    break;
                }
            }
            if (!found) return "This node does not accept any inputs of the given type: " + inputType.getSimpleName();
        }


        inputNodePositions.add(Tools.getLong(inputNode.y, inputNode.x));
        inputNode.outputNodePositions.add(Tools.getLong(y, x));
        action.EVENT_ENDPOINT_NODES.get(eventName).removeAll(inputNode);

        if (!inputLoopCheck(action, new ArrayList<>()))
        {
            removeInput(action, inputNode);
            return "Infinite loop";
        }

        return null;
    }

    //Passing an action here because it needs to be usable from client, which doesn't have the action database
    public final void removeInput(CAction action, CNode inputNode)
    {
        inputNodePositions.remove(Tools.getLong(inputNode.y, inputNode.x));
        inputNode.outputNodePositions.remove(Tools.getLong(y, x));
        if (inputNode.outputNodePositions.size() == 0) action.EVENT_ENDPOINT_NODES.get(eventName).add(inputNode, Tools.getLong(inputNode.y, inputNode.x));
    }


    //Passing an action here because it needs to be usable from client, which doesn't have the action database
    public final void delete(CAction action)
    {
        long pos = Tools.getLong(y, x);

        action.EVENT_NODES.get(eventName).remove(pos);
        ExplicitPriorityQueue<CNode> endPoints = action.EVENT_ENDPOINT_NODES.get(eventName);
        endPoints.removeAll(this);

        for (Long position : inputNodePositions)
        {
            CNode other = action.EVENT_NODES.get(eventName).get(position);
            other.outputNodePositions.remove(pos);
            if (other.outputNodePositions.size() == 0) endPoints.add(other, Tools.getLong(other.y, other.x));
        }

        for (Long position : outputNodePositions)
        {
            CNode other = action.EVENT_NODES.get(eventName).get(position);
            other.inputNodePositions.remove(pos);
        }
    }


    //Passing an action here because it needs to be usable from client, which doesn't have the action database
    public final void setPosition(CAction action, int x, int y, GUINode nodeElement)
    {
        long oldPos = Tools.getLong(this.y, this.x), newPos = Tools.getLong(y, x);

        this.x = x;
        this.y = y;


        for (Long position : inputNodePositions)
        {
            CNode other = action.EVENT_NODES.get(eventName).get(position);
            int index = other.outputNodePositions.indexOf(oldPos);
            other.outputNodePositions.remove(oldPos);
            other.outputNodePositions.add(index, newPos);
        }

        for (Long position : outputNodePositions)
        {
            CNode other = action.EVENT_NODES.get(eventName).get(position);
            int index = other.inputNodePositions.indexOf(oldPos);
            other.inputNodePositions.remove(oldPos);
            other.inputNodePositions.add(index, newPos);
        }


        action.EVENT_NODES.get(eventName).remove(oldPos);
        action.EVENT_NODES.get(eventName).put(newPos, this);


        ((EventEditorGUI) nodeElement.screen).refreshNodeConnections();
    }


    //Passing an action here because it needs to be usable from client, which doesn't have the action database
    public final String error(CAction action)
    {
        LinkedHashMap<Long, CNode> eventNodes = action.EVENT_NODES.get(eventName);
        if (eventNodes == null) return "No action event found with name: " + '"' + eventName + '"';

        if (inputNodePositions.size() < requiredInputTypes().length) return "Required input count not met; need more inputs";

        Class c1, c2;
        for (int i = 0; i < requiredInputTypes().length; i++)
        {
            CNode inputNode = eventNodes.get(inputNodePositions.get(i));
            if (inputNode == null) return "Node for input connection #" + (i + 1) + " does not exist!";

            c1 = requiredInputTypes()[i];
            c2 = inputNode.outputType();
            if (!Tools.areRelated(c1, c2)) return "Input #" + (i + 1) + " requires a " + c1.getSimpleName() + " but is being passed a " + c2.getSimpleName();
        }

        return null;
    }


    //Passing an action here because it needs to be usable from client, which doesn't have the action database
    public final boolean inputLoopCheck(CAction action, ArrayList<CNode> inputBlacklist)
    {
        if (inputBlacklist.contains(this)) return false;

        inputBlacklist.add(this);
        for (Long inputPosition : inputNodePositions)
        {
            CNode input = action.EVENT_NODES.get(eventName).get(inputPosition);
            if (!input.inputLoopCheck(action, inputBlacklist)) return false;
        }

        inputBlacklist.remove(this);
        return true;
    }


    public final Object executeTree(CAction mainAction, HashMap<Long, Object> results)
    {
        Object[] inputResults = new Object[inputNodePositions.size()];

        CAction action = CAction.ALL_ACTIONS.get(actionName);

        int i = 0;
        for (long position : inputNodePositions)
        {
            if (!action.active) return null;

            inputResults[i++] = results.computeIfAbsent(position, o -> action.EVENT_NODES.get(eventName).get(position).executeTree(mainAction, results));
        }

        return execute(mainAction, inputResults);
    }

    protected abstract Object execute(CAction mainAction, Object... inputs);


    @SideOnly(Side.CLIENT)
    public GUIScreen showNodeEditGUI()
    {
        return null;
    }


    @Override
    public CNode write(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, actionName);
        ByteBufUtils.writeUTF8String(buf, eventName);

        buf.writeInt(x);
        buf.writeInt(y);

        buf.writeInt(inputNodePositions.size());
        for (Long position : inputNodePositions)
        {
            buf.writeLong(position);
        }

        buf.writeInt(outputNodePositions.size());
        for (Long position : outputNodePositions)
        {
            buf.writeLong(position);
        }

        return this;
    }

    @Override
    public CNode read(ByteBuf buf)
    {
        actionName = ByteBufUtils.readUTF8String(buf);
        eventName = ByteBufUtils.readUTF8String(buf);

        x = buf.readInt();
        y = buf.readInt();

        inputNodePositions.clear();
        for (int i = buf.readInt(); i > 0; i--) inputNodePositions.add(buf.readLong());

        outputNodePositions.clear();
        for (int i = buf.readInt(); i > 0; i--) outputNodePositions.add(buf.readLong());

        return this;
    }

    @Override
    public CNode save(OutputStream stream)
    {
        new CStringUTF8().set(actionName).save(stream).set(eventName).save(stream);

        CInt ci = new CInt().set(x).save(stream).set(y).save(stream).set(inputNodePositions.size()).save(stream);
        CLong cl = new CLong();
        for (Long position : inputNodePositions) cl.set(position).save(stream);

        ci.set(outputNodePositions.size()).save(stream);
        for (Long position : outputNodePositions) cl.set(position).save(stream);

        return this;
    }

    @Override
    public CNode load(InputStream stream)
    {
        CStringUTF8 cs = new CStringUTF8();
        CInt ci = new CInt();
        CLong cl = new CLong();

        actionName = cs.load(stream).value;
        eventName = cs.load(stream).value;

        x = ci.load(stream).value;
        y = ci.load(stream).value;

        inputNodePositions.clear();
        for (int i = ci.load(stream).value; i > 0; i--) inputNodePositions.add(cl.load(stream).value);

        outputNodePositions.clear();
        for (int i = ci.load(stream).value; i > 0; i--) outputNodePositions.add(cl.load(stream).value);

        return this;
    }
}
