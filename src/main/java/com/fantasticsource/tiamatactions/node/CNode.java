package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.component.CInt;
import com.fantasticsource.tools.component.Component;
import com.fantasticsource.tools.datastructures.Pair;
import io.netty.buffer.ByteBuf;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public abstract class CNode extends Component
{
    public CAction action;
    public String event;
    public int x, y;

    public CNode[] inputNodes;


    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNode()
    {
    }

    public CNode(CAction action)
    {
        this.action = action;
    }

    public abstract String getDescription();


    public abstract Class[] requiredInputTypes();

    public abstract Class arrayInputType();

    public abstract Class outputType();


    public final Object executeTree(CAction parentAction, HashMap<CNode, Object> results)
    {
        Object[] inputResults = new Object[inputNodes.length];

        int i = 0;
        for (CNode inputNode : inputNodes)
        {
            if (!action.valid) return null;

            inputResults[i++] = results.computeIfAbsent(inputNode, o -> inputNode.executeTree(parentAction, results));
        }

        return execute(parentAction, inputResults);
    }

    protected abstract Object execute(CAction parentAction, Object... inputs);


    public GUIScreen getNodeEditGUI()
    {
        return null;
    }


    @Override
    public CNode write(ByteBuf buf)
    {
        buf.writeInt(inputNodes.length);
        for (CNode inputNode : inputNodes)
        {
            buf.writeInt(inputNode.x);
            buf.writeInt(inputNode.y);
        }

        return this;
    }

    @Override
    public CNode read(ByteBuf buf)
    {
        inputNodes = new CNode[buf.readInt()];
        for (int i = 0; i < inputNodes.length; i++)
        {
            inputNodes[i] = action.EVENT_NODES.get(event).get(new Pair<>(buf.readInt(), buf.readInt()));
        }

        return this;
    }

    @Override
    public CNode save(OutputStream stream)
    {
        CInt ci = new CInt().set(inputNodes.length).save(stream);
        for (CNode inputNode : inputNodes) ci.set(inputNode.x).save(stream).set(inputNode.y).save(stream);

        return this;
    }

    @Override
    public CNode load(InputStream stream)
    {
        CInt ci = new CInt();

        inputNodes = new CNode[ci.load(stream).value];
        for (int i = 0; i < inputNodes.length; i++)
        {
            inputNodes[i] = action.EVENT_NODES.get(event).get(new Pair<>(ci.load(stream).value, ci.load(stream).value));
        }

        return this;
    }
}
