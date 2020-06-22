package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeDebug extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/debug.png");

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeDebug()
    {
        super();
    }

    public CNodeDebug(String actionName, String event, int x, int y)
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
    public Class outputType()
    {
        return null;
    }


    @Override
    public Object execute(CAction parentAction, Object... inputs)
    {
        parentAction.source.sendMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE + "<Input count: " + inputs.length + ">"));
        for (Object input : inputs) parentAction.source.sendMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE + input.toString()));

        return null;
    }
}
