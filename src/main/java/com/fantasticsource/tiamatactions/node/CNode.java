package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.component.Component;
import io.netty.buffer.ByteBuf;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public abstract class CNode extends Component
{
    public CAction action;
    public ArrayList<CNode>[] inputNodes, outputNodes;


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

    public abstract Class[] outputTypes();

    public abstract Object[] execute(CAction parentAction, Object... inputs);


    @Override
    public CNode write(ByteBuf buf)
    {
        return this;
    }

    @Override
    public CNode read(ByteBuf buf)
    {
        return this;
    }

    @Override
    public CNode save(OutputStream stream)
    {
        return this;
    }

    @Override
    public CNode load(InputStream stream)
    {
        return this;
    }
}
