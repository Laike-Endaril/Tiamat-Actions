package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodePlaySoundAtPosition extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/play_sound_at_position.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("sound", Object.class);
        REQUIRED_INPUTS.put("dimension", Object.class);
        REQUIRED_INPUTS.put("x", Object.class);
        REQUIRED_INPUTS.put("y", Object.class);
        REQUIRED_INPUTS.put("z", Object.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodePlaySoundAtPosition()
    {
        super();
    }

    public CNodePlaySoundAtPosition(String actionName, String event, int x, int y)
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
        return "Play a sound at a given position";
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
        MCTools.playSimpleSoundAtPosition(new ResourceLocation("" + inputs[0]), Integer.parseInt("" + inputs[1]), Float.parseFloat("" + inputs[1]), Float.parseFloat("" + inputs[1]), Float.parseFloat("" + inputs[1]));

        return null;
    }
}
