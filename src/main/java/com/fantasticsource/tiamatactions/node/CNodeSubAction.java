package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.component.CStringUTF8;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.InputStream;
import java.io.OutputStream;

public class CNodeSubAction extends CNode
{
    public String subActionName;

    @Override
    public String getDescription()
    {
        return "Run another action as part of this action";
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
        return null;
    }


    @Override
    public Object execute(CAction parentAction, Object... inputs)
    {
        CAction subAction = CAction.ALL_ACTIONS.get(subActionName);
        if (subAction == null || subAction.tickEndpointNodes.size() > 0) throw new IllegalArgumentException("Cannot run actions with tick tasks as sub-actions!");

        subAction.queue(parentAction.source, parentAction.queue.name, parentAction.mainAction);

        return null;
    }


    @Override
    public CNodeSubAction write(ByteBuf buf)
    {
        super.write(buf);

        ByteBufUtils.writeUTF8String(buf, subActionName);

        return this;
    }

    @Override
    public CNodeSubAction read(ByteBuf buf)
    {
        super.read(buf);

        subActionName = ByteBufUtils.readUTF8String(buf);

        return this;
    }

    @Override
    public CNodeSubAction save(OutputStream stream)
    {
        super.save(stream);

        new CStringUTF8().set(subActionName).save(stream);

        return this;
    }

    @Override
    public CNodeSubAction load(InputStream stream)
    {
        super.load(stream);

        subActionName = new CStringUTF8().load(stream).value;

        return this;
    }

}
