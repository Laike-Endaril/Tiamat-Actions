package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.ImprovedRayTracing;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.data.Ray;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeBlocksInRay extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/blocks_in_ray.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("world", World.class);
        REQUIRED_INPUTS.put("ray", Ray.class);
        REQUIRED_INPUTS.put("maxDistance", Object.class);
        REQUIRED_INPUTS.put("collideOnAllSolids", Boolean.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeBlocksInRay()
    {
        super();
    }

    public CNodeBlocksInRay(String actionName, String event, int x, int y)
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
        return "Find blocks that intersect a ray";
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
        return BlockPos[].class;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        Ray ray = (Ray) inputs[1];
        return ImprovedRayTracing.blocksInRay((World) inputs[0], ray.origin, ray.origin.add(ray.direction), Double.parseDouble("" + inputs[2]), (boolean) inputs[3]);
    }
}
