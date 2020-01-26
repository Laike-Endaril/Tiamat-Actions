package com.fantasticsource.tiamatactions.task;

import com.fantasticsource.tiamatactions.action.ActionTaskHandler;
import com.fantasticsource.tiamatactions.gui.TaskGUI;
import com.fantasticsource.tools.component.CStringUTF8;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.InputStream;
import java.io.OutputStream;

public class CTaskCommand extends CTask
{
    public String command = "";


    @Override
    public String getDescription()
    {
        return "Run Command: " + command;
    }

    @Override
    public void tick(ActionTaskHandler handler)
    {
        FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(handler.controller, command.replaceAll("@p|@P", handler.controller.getName()));
        handler.currentTasks.addAll(nextTasks);
    }

    @Override
    public TaskGUI getTaskGUI()
    {
        //TODO
        return null;
    }


    @Override
    public CTaskCommand write(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, command);

        super.write(buf);

        return this;
    }

    @Override
    public CTaskCommand read(ByteBuf buf)
    {
        command = ByteBufUtils.readUTF8String(buf);

        super.read(buf);

        return this;
    }

    @Override
    public CTaskCommand save(OutputStream stream)
    {
        new CStringUTF8().set(command).save(stream);

        super.save(stream);

        return this;
    }

    @Override
    public CTaskCommand load(InputStream stream)
    {
        command = new CStringUTF8().load(stream).value;

        super.load(stream);

        return this;
    }
}
