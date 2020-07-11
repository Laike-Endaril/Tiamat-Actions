package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeBlockstate extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/blockstate.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("blockID", String.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeBlockstate()
    {
        super();
    }

    public CNodeBlockstate(String actionName, String event, int x, int y)
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
        return "Output a blockstate";
    }


    @Override
    public LinkedHashMap<String, Class> getRequiredInputs()
    {
        return REQUIRED_INPUTS;
    }

    @Override
    public Pair<String, Class> getOptionalInputs()
    {
        return null;
    }

    @Override
    public Class outputType()
    {
        return IBlockState.class;
    }


    @Override
    public Object execute(CAction mainAction, Object... inputs)
    {
        String[] tokens = Tools.fixedSplit("" + inputs[0], ":");
        String domain = "minecraft", name;
        int meta = 0;
        if (tokens.length == 1) name = tokens[0];
        else if (tokens.length == 2)
        {
            try
            {
                meta = Integer.parseInt(tokens[1]);
                name = tokens[0];
            }
            catch (NumberFormatException e)
            {
                domain = tokens[0];
                name = tokens[1];
            }
        }
        else
        {
            domain = tokens[0];
            name = tokens[1];
            meta = Integer.parseInt(tokens[2]);
        }
        return ForgeRegistries.BLOCKS.getValue(new ResourceLocation(domain, name)).getStateFromMeta(meta);
    }
}
