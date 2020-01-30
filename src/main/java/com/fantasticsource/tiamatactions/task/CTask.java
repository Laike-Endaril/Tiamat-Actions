package com.fantasticsource.tiamatactions.task;

import com.fantasticsource.tiamatactions.action.ActionTaskHandler;
import com.fantasticsource.tiamatactions.action.CAction;
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
    public String owningAction = "", ranFromAction = "";
    public boolean stopOnActionInterrupt = true;
    private ArrayList<CTask> nextTasks = new ArrayList<>();


    public abstract String getDescription();


    public abstract void tick(ActionTaskHandler handler);


    public abstract TaskGUI getTaskGUI();


    public final CTask setRanFromAction(CAction ranFromAction)
    {
        return setRanFromAction(ranFromAction.name);
    }

    protected final CTask setRanFromAction(String ranFromActionName)
    {
        ranFromAction = ranFromActionName;
        return this;
    }


    public final CTask queueTask(CTask task)
    {
        nextTasks.add(task.setRanFromAction(this.ranFromAction));
        return this;
    }


    @Override
    public CTask write(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, owningAction);
        ByteBufUtils.writeUTF8String(buf, ranFromAction);
        buf.writeBoolean(stopOnActionInterrupt);

        buf.writeInt(nextTasks.size());
        for (CTask task : nextTasks) writeMarked(buf, task);

        return this;
    }

    @Override
    public CTask read(ByteBuf buf)
    {
        owningAction = ByteBufUtils.readUTF8String(buf);
        ranFromAction = ByteBufUtils.readUTF8String(buf);
        stopOnActionInterrupt = buf.readBoolean();

        nextTasks.clear();
        for (int i = buf.readInt(); i > 0; i--) nextTasks.add((CTask) readMarked(buf));

        return this;
    }

    @Override
    public CTask save(OutputStream stream)
    {
        CStringUTF8 cs = new CStringUTF8();
        cs.set(owningAction).save(stream);
        cs.set(ranFromAction).save(stream);
        new CBoolean().set(stopOnActionInterrupt).save(stream);

        new CInt().set(nextTasks.size()).save(stream);
        for (CTask task : nextTasks) saveMarked(stream, task);

        return this;
    }

    @Override
    public CTask load(InputStream stream)
    {
        CStringUTF8 cs = new CStringUTF8();
        owningAction = cs.load(stream).value;
        ranFromAction = cs.load(stream).value;
        stopOnActionInterrupt = new CBoolean().load(stream).value;

        nextTasks.clear();
        for (int i = new CInt().load(stream).value; i > 0; i--) nextTasks.add((CTask) loadMarked(stream));
        return this;
    }
}
