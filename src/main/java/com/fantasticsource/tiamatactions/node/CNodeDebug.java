package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CNodeDebug extends CNode
{
    @Override
    public String getDescription()
    {
        return "Display one or more values";
    }

    @Override
    public Class[] requiredInputTypes()
    {
        return new Class[0];
    }

    @Override
    public Class arrayInputType()
    {
        return Object.class;
    }

    @Override
    public Class[] outputTypes()
    {
        return new Class[]{};
    }

    @Override
    public Object[] execute(CAction parentAction, Object... inputs)
    {
        if (inputs.length > 1) parentAction.source.sendMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE + "<Input count: " + inputs.length + ">"));
        for (Object input : inputs) parentAction.source.sendMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE + input.toString()));

        return new Object[0];
    }
}
