package com.fantasticsource.tiamatactions.task;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.gui.TaskGUI;
import com.fantasticsource.tools.component.CStringUTF8;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.InputStream;
import java.io.OutputStream;

public class CTaskSubAction extends CTask
{
    public String subActionName;

    @Override
    public String getDescription()
    {
        return "Run another action as part of this action";
    }

    @Override
    public void execute(CAction parentAction)
    {
        CAction subAction = CAction.ALL_ACTIONS.get(subActionName);
        if (subAction == null || subAction.tickTasks.size() > 0) throw new IllegalArgumentException("Cannot run actions with tick tasks as sub-actions!");

        subAction.queue(parentAction.source, parentAction.queue.name, parentAction.mainAction);
    }

    @Override
    public TaskGUI getTaskGUI()
    {
        //TODO
        return null;
    }


    @Override
    public CTaskSubAction write(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, subActionName);

        return this;
    }

    @Override
    public CTaskSubAction read(ByteBuf buf)
    {
        subActionName = ByteBufUtils.readUTF8String(buf);

        return this;
    }

    @Override
    public CTaskSubAction save(OutputStream stream)
    {
        new CStringUTF8().set(subActionName).save(stream);

        return this;
    }

    @Override
    public CTaskSubAction load(InputStream stream)
    {
        subActionName = new CStringUTF8().load(stream).value;

        return this;
    }

}
