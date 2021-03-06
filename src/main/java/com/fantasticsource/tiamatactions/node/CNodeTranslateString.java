package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeTranslateString extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/translate_string.png");
    protected static final Pair<String, Class> OPTIONAL_INPUTS = new Pair<>("args", Object.class);
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("langKeyOrFormat", Object.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeTranslateString()
    {
        super();
    }

    public CNodeTranslateString(String actionName, String event, int x, int y)
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
        return "Format a string and/or translate a lang key and its arguments to a localized string";
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
        return String.class;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        String key = "" + inputs[0];
        Object[] args = new Object[inputs.length - 1];
        System.arraycopy(inputs, 1, args, 0, args.length);

        if (I18n.canTranslate(key)) return I18n.translateToLocalFormatted(key, args); //Lang translation


        for (int i = 0; i < args.length; i++)
        {
            try
            {
                args[i] = Double.parseDouble("" + args[i]);
            }
            catch (NumberFormatException e)
            {
                continue;
            }

            try
            {
                args[i] = Integer.parseInt("" + args[i]);
            }
            catch (NumberFormatException e)
            {
            }
        }

        StringBuilder s = new StringBuilder(I18n.translateToLocalFormatted(key, args)); //String.format() if applicable
        if (args.length > 0 && s.toString().equals(key)) //Untranslated key (append args in parens)
        {
            s.append("(").append(args[0]);
            for (int i = 1; i < args.length; i++) s.append(", ").append(args[i]);
            s.append(")");
        }

        return s.toString();
    }
}
