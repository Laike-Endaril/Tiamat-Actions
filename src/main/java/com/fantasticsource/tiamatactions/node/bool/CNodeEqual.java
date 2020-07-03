package com.fantasticsource.tiamatactions.node.bool;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeEqual extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/get_action_var.png");
    protected static final Pair<String, Class> OPTIONAL_INPUTS = new Pair<>("moreValues", Object.class);
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("value1", Object.class);
        REQUIRED_INPUTS.put("value2", Object.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeEqual()
    {
        super();
    }

    public CNodeEqual(String actionName, String event, int x, int y)
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
        return "Do all inputs have the same value?";
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
        return Boolean.class;
    }


    @Override
    public Object execute(CAction mainAction, Object... inputs)
    {
        if (inputs[0] == null)
        {
            for (int i = 1; i < inputs.length; i++)
            {
                if (inputs[i] != null) return false;
            }
            return true;
        }
        else
        {
            for (int i = 1; i < inputs.length; i++)
            {
                if (!inputs[0].equals(inputs[i])) return false;
            }
            return true;
        }
    }
}
