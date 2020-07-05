package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeCommand extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/command.png");
    protected static final Pair<String, Class> OPTIONAL_INPUTS = new Pair<>("args", Object.class);
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("command", Object.class);
    }

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
    public LinkedHashMap<String, Class> getRequiredInputs()
    {
        return REQUIRED_INPUTS;
    }

    @Override
    public Pair<String, Class> getOptionalInputs()
    {
        return OPTIONAL_INPUTS;
    }

    @Override
    public Class outputType()
    {
        return null;
    }


    @Override
    public Object execute(CAction mainAction, Object... inputs)
    {
        String command = ("" + inputs[0]).replaceAll("@p|@P", mainAction.source.getName());

        for (int i = 1; i < inputs.length; i++)
        {
            command = command.replaceAll("@" + i, "" + inputs[i]);
        }

        FMLCommonHandler.instance().getMinecraftServerInstance().commandManager.executeCommand(mainAction.source, command);

        return null;
    }
}
