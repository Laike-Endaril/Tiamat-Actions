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
    public Class[] outputTypes()
    {
        return new Class[]{};
    }

    @Override
    public Object[] execute(CAction parentAction, Object... inputs)
    {
        CAction subAction = CAction.ALL_ACTIONS.get(subActionName);
        if (subAction == null || subAction.tickTasks.size() > 0) throw new IllegalArgumentException("Cannot run actions with tick tasks as sub-actions!");

        subAction.queue(parentAction.source, parentAction.queue.name, parentAction.mainAction);

        return new Object[0];
    }


    @Override
    public CNodeSubAction write(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, subActionName);

        return this;
    }

    @Override
    public CNodeSubAction read(ByteBuf buf)
    {
        subActionName = ByteBufUtils.readUTF8String(buf);

        return this;
    }

    @Override
    public CNodeSubAction save(OutputStream stream)
    {
        new CStringUTF8().set(subActionName).save(stream);

        return this;
    }

    @Override
    public CNodeSubAction load(InputStream stream)
    {
        subActionName = new CStringUTF8().load(stream).value;

        return this;
    }

}
