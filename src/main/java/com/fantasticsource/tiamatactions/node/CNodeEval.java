package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.TiamatActions;
import com.fantasticsource.tiamatactions.action.CAction;

import javax.script.ScriptException;

public class CNodeEval extends CNode
{
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


    public String expression = "";

    @Override
    public String getDescription()
    {
        return "Evaluate an expression and output the result";
    }


    @Override
    public Class[] requiredInputTypes()
    {
        return new Class[]{};
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
    public Object execute(CAction parentAction, Object... inputs)
    {
        String expression = this.expression;
        for (int i = 0; i < inputs.length; i++) expression = expression.replaceAll("@" + i, inputs[i].toString());

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
