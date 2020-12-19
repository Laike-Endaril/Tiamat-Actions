package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.ReflectionTool;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeSetFieldValue extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/set_field_value.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("class", Class.class);
        REQUIRED_INPUTS.put("fieldName", Object.class);
        REQUIRED_INPUTS.put("object", Object.class);
        REQUIRED_INPUTS.put("value", Object.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeSetFieldValue()
    {
        super();
    }

    public CNodeSetFieldValue(String actionName, String event, int x, int y)
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
        return "Set the value of a field";
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
        return null;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        Class cls = (Class) inputs[0];
        String fieldName = "" + inputs[1];
        Field field = ReflectionTool.getField(cls, fieldName);
        Class fieldClass = field.getType();

        if (fieldClass == double.class || fieldClass == Double.class) ReflectionTool.set(cls, fieldName, inputs[2], Double.parseDouble("" + inputs[3]));
        else if (fieldClass == float.class || fieldClass == Float.class) ReflectionTool.set(cls, fieldName, inputs[2], Float.parseFloat("" + inputs[3]));
        else if (fieldClass == int.class || fieldClass == Integer.class) ReflectionTool.set(cls, fieldName, inputs[2], Integer.parseInt("" + inputs[3]));
        else if (fieldClass == long.class || fieldClass == Long.class) ReflectionTool.set(cls, fieldName, inputs[2], Long.parseLong("" + inputs[3]));
        else if (fieldClass == short.class || fieldClass == Short.class) ReflectionTool.set(cls, fieldName, inputs[2], Short.parseShort("" + inputs[3]));
        else if (fieldClass == byte.class || fieldClass == Byte.class) ReflectionTool.set(cls, fieldName, inputs[2], Byte.parseByte("" + inputs[3]));
        else ReflectionTool.set(cls, fieldName, inputs[2], inputs[3]);

        return null;
    }
}
