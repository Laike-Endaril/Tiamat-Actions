package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeDebug extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/debug.png");
    protected static final Pair<String, Class> OPTIONAL_INPUTS = new Pair<>("objects", Object.class);
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

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
        if (inputs.length == 0) mainAction.source.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE + "(Nothing)"));

        for (String s : expandArray(inputs)) mainAction.source.sendMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE + s));

        return null;
    }

    protected ArrayList<String> expandArray(Object[] array)
    {
        return expandArray("", array);
    }

    protected ArrayList<String> expandArray(String prefix, Object[] array)
    {
        ArrayList<String> result = new ArrayList<>();

        for (Object o : array)
        {
            if (o instanceof NBTBase)
            {
                for (String s : MCTools.legibleNBT((NBTBase) o)) result.add(prefix + s);
                continue;
            }

            result.add("" + o);
            if (o != null && o.getClass().isArray()) result.addAll(expandArray(prefix + " ", (Object[]) o));
        }

        return result;
    }
}
