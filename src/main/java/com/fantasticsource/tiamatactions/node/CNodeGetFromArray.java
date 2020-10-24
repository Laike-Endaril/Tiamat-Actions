package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.data.Ray;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeGetFromArray extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/get_from_array.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("array", Object[].class);
        REQUIRED_INPUTS.put("number", Object.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeGetFromArray()
    {
        super();
    }

    public CNodeGetFromArray(String actionName, String event, int x, int y)
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
        return "Get the Nth thing from an array";
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
        return Object.class;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        return ((Object[]) inputs[0])[Integer.parseInt("" + inputs[1]) - 1];
    }
}
