package com.fantasticsource.tiamatactions.task;

import com.fantasticsource.tiamatactions.action.ActionTaskHandler;
import com.fantasticsource.tiamatactions.gui.TaskGUI;
import com.fantasticsource.tools.component.CBoolean;
import com.fantasticsource.tools.component.CInt;
import com.fantasticsource.tools.component.CStringUTF8;
import com.fantasticsource.tools.component.Component;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public abstract class CTask extends Component
{
    public String actionName = "";
    public boolean stopOnActionInterrupt = true;
    public ArrayList<CTask> nextTasks = new ArrayList<>();


    public abstract String getDescription();


    public abstract void tick(ActionTaskHandler handler);


    public abstract TaskGUI getTaskGUI();


    @Override
    public CTask write(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, actionName);
        buf.writeBoolean(stopOnActionInterrupt);

        buf.writeInt(nextTasks.size());
        for (CTask task : nextTasks) writeMarked(buf, task);

        return this;
    }

    @Override
    public CTask read(ByteBuf buf)
    {
        actionName = ByteBufUtils.readUTF8String(buf);
        stopOnActionInterrupt = buf.readBoolean();

        nextTasks.clear();
        for (int i = buf.readInt(); i > 0; i--) nextTasks.add((CTask) readMarked(buf));

        return this;
    }

    @Override
    public CTask save(OutputStream stream)
    {
        new CStringUTF8().set(actionName).save(stream);
        new CBoolean().set(stopOnActionInterrupt).save(stream);

        new CInt().set(nextTasks.size()).save(stream);
        for (CTask task : nextTasks) saveMarked(stream, task);

        return this;
    }

    @Override
    public CTask load(InputStream stream)
    {
        actionName = new CStringUTF8().load(stream).value;
        stopOnActionInterrupt = new CBoolean().load(stream).value;

        nextTasks.clear();
        for (int i = new CInt().load(stream).value; i > 0; i--) nextTasks.add((CTask) loadMarked(stream));
        return this;
    }
}
