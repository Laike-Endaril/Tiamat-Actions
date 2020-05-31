package com.fantasticsource.tiamatactions.task;

import com.fantasticsource.tiamatactions.action.CAction;
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
    public void execute(CAction action)
    {
        FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(action.source, command.replaceAll("@p|@P", action.source.getName()));
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

        return this;
    }

    @Override
    public CTaskCommand read(ByteBuf buf)
    {
        command = ByteBufUtils.readUTF8String(buf);

        return this;
    }

    @Override
    public CTaskCommand save(OutputStream stream)
    {
        new CStringUTF8().set(command).save(stream);

        return this;
    }

    @Override
    public CTaskCommand load(InputStream stream)
    {
        command = new CStringUTF8().load(stream).value;

        return this;
    }
}
