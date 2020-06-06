package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.component.CStringUTF8;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class CNodeCommand extends CNode
{
    public String command = "";


    @Override
    public String getDescription()
    {
        return "Run Command: " + command;
    }


    @Override
    public Class[] requiredInputTypes()
    {
        return new Class[0];
    }

    @Override
    public Class arrayInputType()
    {
        return String.class;
    }

    @Override
    public Class outputType()
    {
        return null;
    }


    @Override
    public Object execute(CAction parentAction, Object... inputs)
    {
        String command = this.command.replaceAll("@p|@P", parentAction.source.getName());

        if (inputs.length > 0)
        {
            int i = 0;
            for (String arg : (ArrayList<String>) inputs[0])
            {
                command = command.replaceAll("@" + i, arg);
                i++;
            }
        }

        FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(parentAction.source, command);

        return null;
    }


    @Override
    public CNodeCommand write(ByteBuf buf)
    {
        super.write(buf);

        ByteBufUtils.writeUTF8String(buf, command);

        return this;
    }

    @Override
    public CNodeCommand read(ByteBuf buf)
    {
        super.read(buf);

        command = ByteBufUtils.readUTF8String(buf);

        return this;
    }

    @Override
    public CNodeCommand save(OutputStream stream)
    {
        super.save(stream);

        new CStringUTF8().set(command).save(stream);

        return this;
    }

    @Override
    public CNodeCommand load(InputStream stream)
    {
        super.load(stream);

        command = new CStringUTF8().load(stream).value;

        return this;
    }
}
