package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CNodeCommand extends CNode
{
    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeCommand()
    {
        super();
    }

    public CNodeCommand(String actionName, String event, int x, int y)
    {
        super(actionName, event, x, y);
    }


    @Override
    public String getDescription()
    {
        return "Run a command";
    }


    @Override
    public Class[] requiredInputTypes()
    {
        return new Class[]{String.class};
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
        String command = ((String) inputs[0]).replaceAll("@p|@P", parentAction.source.getName());

        for (int i = 1; i < inputs.length; i++)
        {
            command = command.replaceAll("@" + i, (String) inputs[i]);
        }

        FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(parentAction.source, command);

        return null;
    }
}
