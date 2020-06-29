package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeCommand extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/command.png");

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
    public ResourceLocation getTexture()
    {
        return TEXTURE;
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
    public Object execute(CAction mainAction, Object... inputs)
    {
        String command = ((String) inputs[0]).replaceAll("@p|@P", mainAction.source.getName());

        for (int i = 1; i < inputs.length; i++)
        {
            command = command.replaceAll("@" + i, (String) inputs[i]);
        }

        FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(mainAction.source, command);

        return null;
    }
}
