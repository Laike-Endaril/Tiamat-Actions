package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.EntityFilters;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.data.Ray;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeEntityFilterInCone extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/entities_in_cone.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("coneAxis", Ray.class);
        REQUIRED_INPUTS.put("range", Object.class);
        REQUIRED_INPUTS.put("angle", Object.class);
        REQUIRED_INPUTS.put("requireLOS", Boolean.class);
        REQUIRED_INPUTS.put("entities", Entity[].class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeEntityFilterInCone()
    {
        super();
    }

    public CNodeEntityFilterInCone(String actionName, String event, int x, int y)
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
        return "Only keep entities that collide with a given cone";
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
        return Entity[].class;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        Ray coneAxis = (Ray) inputs[0];
        ArrayList<Entity> entities = new ArrayList<>(Arrays.asList((Entity[]) inputs[4]));
        return EntityFilters.inCone(coneAxis.origin, coneAxis.direction, Double.parseDouble("" + inputs[1]), Double.parseDouble("" + inputs[2]), (boolean) inputs[3], entities).toArray(new Entity[0]);
    }
}
