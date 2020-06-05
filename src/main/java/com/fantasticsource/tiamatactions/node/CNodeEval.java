package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.TiamatActions;
import com.fantasticsource.tiamatactions.action.CAction;

import javax.script.ScriptException;

public class CNodeEval extends CNode
{
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
        return Number.class;
    }

    @Override
    public Class[] outputTypes()
    {
        return new Class[]{Object.class};
    }

    @Override
    public Object[] execute(CAction parentAction, Object... inputs)
    {
        String expression = this.expression;
        for (int i = 0; i < inputs.length; i++) expression = expression.replaceAll("@" + i, inputs[i].toString());

        try
        {
            return new Object[]{TiamatActions.JAVASCRIPT_ENGINE.eval(expression)};
        }
        catch (ScriptException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
