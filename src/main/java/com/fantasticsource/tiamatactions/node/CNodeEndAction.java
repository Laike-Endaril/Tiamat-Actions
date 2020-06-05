package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;

public class CNodeEndAction extends CNode
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
}
