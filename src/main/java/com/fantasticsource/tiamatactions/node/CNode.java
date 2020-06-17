package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.component.CInt;
import com.fantasticsource.tools.component.CLong;
import com.fantasticsource.tools.component.CStringUTF8;
import com.fantasticsource.tools.component.Component;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class CNode extends Component
{
    public String actionName, eventName;
    public int x, y;

    public ArrayList<Long> inputNodePositions = new ArrayList<>();


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


    public abstract String getDescription();


    public abstract Class[] requiredInputTypes();

    public abstract Class arrayInputType();

    public abstract Class outputType();


    public final Object executeTree(CAction parentAction, HashMap<Long, Object> results)
    {
        Object[] inputResults = new Object[inputNodePositions.size()];

        CAction action = CAction.ALL_ACTIONS.get(actionName);

        int i = 0;
        for (long position : inputNodePositions)
        {
            if (!action.active) return null;

            inputResults[i++] = results.computeIfAbsent(position, o -> action.EVENT_NODES.get(eventName).get(position).executeTree(parentAction, results));
        }

        return execute(parentAction, inputResults);
    }

    protected abstract Object execute(CAction parentAction, Object... inputs);


    @SideOnly(Side.CLIENT)
    public GUIScreen getNodeEditGUI()
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

        return this;
    }

    @Override
    public CNode save(OutputStream stream)
    {
        new CStringUTF8().set(actionName).save(stream).set(eventName).save(stream);

        new CInt().set(x).save(stream).set(y).save(stream).set(inputNodePositions.size()).save(stream);
        CLong cl = new CLong();
        for (Long position : inputNodePositions) cl.set(position).save(stream);

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

        return this;
    }
}
