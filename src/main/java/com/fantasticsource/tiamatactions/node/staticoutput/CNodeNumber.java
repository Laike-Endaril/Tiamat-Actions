package com.fantasticsource.tiamatactions.node.staticoutput;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.component.CDouble;
import io.netty.buffer.ByteBuf;

import java.io.InputStream;
import java.io.OutputStream;

public class CNodeNumber extends CNode
{
    public double number;


    @Override
    public String getDescription()
    {
        return "Output a static number";
    }


    @Override
    public Class[] requiredInputTypes()
    {
        return new Class[0];
    }

    @Override
    public Class arrayInputType()
    {
        return null;
    }

    @Override
    public Class outputType()
    {
        return String.class;
    }


    @Override
    public Object execute(CAction parentAction, Object... inputs)
    {
        return action.source;
    }


    @Override
    public CNodeNumber write(ByteBuf buf)
    {
        super.write(buf);

        buf.writeDouble(number);

        return this;
    }

    @Override
    public CNodeNumber read(ByteBuf buf)
    {
        super.read(buf);

        number = buf.readDouble();

        return this;
    }

    @Override
    public CNodeNumber save(OutputStream stream)
    {
        super.save(stream);

        new CDouble().set(number).save(stream);

        return this;
    }

    @Override
    public CNodeNumber load(InputStream stream)
    {
        super.load(stream);

        number = new CDouble().load(stream).value;

        return this;
    }
}
