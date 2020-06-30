package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.TiamatActions;
import com.fantasticsource.tiamatactions.action.CAction;
import net.minecraft.util.ResourceLocation;

import javax.script.ScriptException;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeEval extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/eval.png");

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeEval()
    {
        super();
    }

    public CNodeEval(String actionName, String event, int x, int y)
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
        return "Evaluate an expression and output the result";
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
        return Object.class;
    }


    @Override
    public Object execute(CAction mainAction, Object... inputs)
    {
        String expression = (String) inputs[0];
        for (int i = 1; i < inputs.length; i++) expression = expression.replaceAll("@" + i, inputs[i].toString());

        try
        {
            return TiamatActions.JAVASCRIPT_ENGINE.eval(expression);
        }
        catch (ScriptException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
