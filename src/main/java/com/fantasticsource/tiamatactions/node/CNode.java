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
import com.fantasticsource.tools.datastructures.Pair;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class CNode extends Component
{
    public String actionName, eventName;
    public int x, y;

    public ArrayList<Long> conditionNodePositions = new ArrayList<>();
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


    public abstract LinkedHashMap<String, Class> getRequiredInputs();

    public abstract Pair<String, Class> getOptionalInputs();

    public abstract Class outputType();


    //Passing an action here because it needs to be usable from client, which doesn't have the action database
    public final String tryAddInput(CAction action, CNode inputNode)
    {
        if (inputNode == this) return "Same node";

        if (inputNode instanceof CNodeTestCondition)
        {
            conditionNodePositions.add(Tools.getLong(inputNode.y, inputNode.x));
            inputNode.outputNodePositions.add(Tools.getLong(y, x));
            action.EVENT_ENDPOINT_NODES.get(eventName).removeAll(inputNode);
        }
        else
        {
            if (getOptionalInputs() == null)
            {
                if (getRequiredInputs().size() == 0) return "This node cannot accept inputs";
                if (inputNodePositions.size() >= getRequiredInputs().size()) return "This node cannot accept any more inputs";
            }

            Class inputType = inputNode.outputType();
            if (inputType == null) return "Input has no output type";

            if (getOptionalInputs() == null || !Tools.areRelated(inputType, getOptionalInputs().getValue()))
            {
                boolean found = false;
                for (Class c : getRequiredInputs().values())
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
        }

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
        conditionNodePositions.remove(Tools.getLong(inputNode.y, inputNode.x));
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

        for (long position : conditionNodePositions)
        {
            CNode other = action.EVENT_NODES.get(eventName).get(position);
            other.outputNodePositions.remove(pos);
            if (other.outputNodePositions.size() == 0) endPoints.add(other, Tools.getLong(other.y, other.x));
        }

        for (long position : inputNodePositions)
        {
            CNode other = action.EVENT_NODES.get(eventName).get(position);
            other.outputNodePositions.remove(pos);
            if (other.outputNodePositions.size() == 0) endPoints.add(other, Tools.getLong(other.y, other.x));
        }

        for (long position : outputNodePositions)
        {
            CNode other = action.EVENT_NODES.get(eventName).get(position);
            other.conditionNodePositions.remove(pos);
            other.inputNodePositions.remove(pos);
        }
    }


    //Passing an action here because it needs to be usable from client, which doesn't have the action database
    public final void setPosition(CAction action, int x, int y, GUINode nodeElement)
    {
        long oldPos = Tools.getLong(this.y, this.x), newPos = Tools.getLong(y, x);

        this.x = x;
        this.y = y;


        if (action.EVENT_ENDPOINT_NODES.get(eventName).removeAll(this)) action.EVENT_ENDPOINT_NODES.get(eventName).add(this, Tools.getLong(y, x));


        for (Long position : conditionNodePositions)
        {
            CNode other = action.EVENT_NODES.get(eventName).get(position);
            int index = other.outputNodePositions.indexOf(oldPos);
            other.outputNodePositions.remove(oldPos);
            other.outputNodePositions.add(index, newPos);
        }

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
            int index = other.inputNodePositions.indexOf(oldPos), index2 = other.conditionNodePositions.indexOf(oldPos);
            if (index != -1)
            {
                other.inputNodePositions.remove(oldPos);
                other.inputNodePositions.add(index, newPos);
            }
            if (index2 != -1)
            {
                other.conditionNodePositions.remove(oldPos);
                other.conditionNodePositions.add(index2, newPos);
            }
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

        if (inputNodePositions.size() < getRequiredInputs().size()) return "Required input count not met; need more inputs";

        Class c2;
        int i = 0;
        for (Class c1 : getRequiredInputs().values())
        {
            CNode inputNode = eventNodes.get(inputNodePositions.get(i++));
            if (inputNode == null) return "Node for input connection #" + (i) + " does not exist!";

            c2 = inputNode.outputType();
            if (!Tools.areRelated(c1, c2)) return "Input #" + (i) + " requires a " + c1.getSimpleName() + " but is being passed a " + c2.getSimpleName();
        }

        return null;
    }


    //Passing an action here because it needs to be usable from client, which doesn't have the action database
    public final boolean inputLoopCheck(CAction action, ArrayList<CNode> inputBlacklist)
    {
        if (inputBlacklist.contains(this)) return false;

        inputBlacklist.add(this);


        for (Long inputPosition : conditionNodePositions)
        {
            CNode input = action.EVENT_NODES.get(eventName).get(inputPosition);
            if (!input.inputLoopCheck(action, inputBlacklist)) return false;
        }

        for (Long inputPosition : inputNodePositions)
        {
            CNode input = action.EVENT_NODES.get(eventName).get(inputPosition);
            if (!input.inputLoopCheck(action, inputBlacklist)) return false;
        }


        inputBlacklist.remove(this);

        return true;
    }


    public final void executeTree(CAction mainAction, CAction subAction, HashMap<Long, Object> results)
    {
        executeTree(mainAction, subAction, results, false);
    }

    public final void executeTree(CAction mainAction, CAction subAction, HashMap<Long, Object> results, boolean force)
    {
        try
        {
            executeTreeInternal(mainAction, subAction, results, force);
        }
        catch (Exception e)
        {
            StackTraceElement[] stackTrace = e.getStackTrace();
            if (mainAction.loggedErrors.add(stackTrace))
            {
                System.err.println(TextFormatting.RED + "Exception caught in action: " + subAction.name + " (Main action: " + mainAction.name + ")");
                e.printStackTrace();

                if (mainAction.source instanceof EntityPlayerMP)
                {
                    mainAction.source.sendMessage(new TextComponentString(TextFormatting.RED + "Exception caught in action: " + subAction.name + " (Main action: " + mainAction.name + ")"));

                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));
                    for (String line : Tools.fixedSplit(errors.toString(), "\n"))
                    {
                        mainAction.source.sendMessage(new TextComponentString(TextFormatting.RED + line.replaceAll("\r", "")));
                    }
                }
            }
        }
    }

    protected final Object executeTreeInternal(CAction mainAction, CAction subAction, HashMap<Long, Object> results, boolean force)
    {
        Object[] inputResults = new Object[inputNodePositions.size()];

        for (long position : conditionNodePositions)
        {
            if (!force && !mainAction.active) return null;

            CNodeTestCondition input = (CNodeTestCondition) subAction.EVENT_NODES.get(eventName).get(position);

            if (input.executeTreeInternal(mainAction, subAction, results, force) == CNodeTestCondition.CANCEL) return CNodeTestCondition.CANCEL;
        }

        int i = 0;
        for (long position : inputNodePositions)
        {
            if (!force && !mainAction.active) return null;

            inputResults[i] = results.computeIfAbsent(position, o -> subAction.EVENT_NODES.get(eventName).get(position).executeTreeInternal(mainAction, subAction, results, force));

            if (inputResults[i++] == CNodeTestCondition.CANCEL) return CNodeTestCondition.CANCEL;
        }

        return execute(mainAction, subAction, inputResults);
    }

    protected abstract Object execute(CAction mainAction, CAction subAction, Object... inputs);


    @Override
    public CNode write(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, actionName);
        ByteBufUtils.writeUTF8String(buf, eventName);

        buf.writeInt(x);
        buf.writeInt(y);

        buf.writeInt(conditionNodePositions.size());
        for (Long position : conditionNodePositions)
        {
            buf.writeLong(position);
        }

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

        conditionNodePositions.clear();
        for (int i = buf.readInt(); i > 0; i--) conditionNodePositions.add(buf.readLong());

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

        CInt ci = new CInt().set(x).save(stream).set(y).save(stream);

        CLong cl = new CLong();

        ci.set(conditionNodePositions.size()).save(stream);
        for (Long position : conditionNodePositions) cl.set(position).save(stream);

        ci.set(inputNodePositions.size()).save(stream);
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

        conditionNodePositions.clear();
        for (int i = ci.load(stream).value; i > 0; i--) conditionNodePositions.add(cl.load(stream).value);

        inputNodePositions.clear();
        for (int i = ci.load(stream).value; i > 0; i--) inputNodePositions.add(cl.load(stream).value);

        outputNodePositions.clear();
        for (int i = ci.load(stream).value; i > 0; i--) outputNodePositions.add(cl.load(stream).value);

        return this;
    }


    @SideOnly(Side.CLIENT)
    public GUIScreen showNodeEditGUI()
    {
        return null;
    }
}
