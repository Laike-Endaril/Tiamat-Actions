package com.fantasticsource.tiamatactions.node.staticoutput;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.component.CStringUTF8;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.InputStream;
import java.io.OutputStream;

public class CNodeString extends CNode
{
    public String string;


    @Override
    public String getDescription()
    {
        return "Output a static string";
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
    public CNodeString write(ByteBuf buf)
    {
        super.write(buf);

        ByteBufUtils.writeUTF8String(buf, string);

        return this;
    }

    @Override
    public CNodeString read(ByteBuf buf)
    {
        super.read(buf);

        string = ByteBufUtils.readUTF8String(buf);

        return this;
    }

    @Override
    public CNodeString save(OutputStream stream)
    {
        super.save(stream);

        new CStringUTF8().set(string).save(stream);

        return this;
    }

    @Override
    public CNodeString load(InputStream stream)
    {
        super.load(stream);

        string = new CStringUTF8().load(stream).value;

        return this;
    }
}
