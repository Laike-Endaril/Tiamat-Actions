package com.fantasticsource.tiamatactions.task;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.gui.TaskGUI;

public class CTaskEndAction extends CTask
{
    @Override
    public String getDescription()
    {
        return "End this action (and run action end tasks if the action has finished all initialization and start tasks)";
    }

    @Override
    public void execute(CAction parentAction)
    {
        parentAction.valid = false;
    }

    @Override
    public TaskGUI getTaskGUI()
    {
        //TODO
        return null;
    }
}
