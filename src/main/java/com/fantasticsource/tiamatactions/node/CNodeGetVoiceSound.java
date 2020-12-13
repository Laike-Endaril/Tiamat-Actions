package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.fantasticlib.api.FLibAPI;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.ReflectionTool;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeGetVoiceSound extends CNode
{
    protected static final LinkedHashMap<String, LinkedHashMap<String, ResourceLocation>> ALL_VOICE_SETS = Loader.isModLoaded("faeruncharacters") ? (LinkedHashMap<String, LinkedHashMap<String, ResourceLocation>>) ReflectionTool.get(ReflectionTool.getField(ReflectionTool.getClassByName("com.fantasticsource.faeruncharacters.VoiceSets"), "ALL_VOICE_SETS"), null) : null;

    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/get_voice_sound.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("entity", Entity.class);
        REQUIRED_INPUTS.put("key", Object.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeGetVoiceSound()
    {
        super();
    }

    public CNodeGetVoiceSound(String actionName, String event, int x, int y)
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
        return "Get a sound from an entity's Faerun Characters voice set";
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
        return String.class;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        if (ALL_VOICE_SETS == null) return null;

        String voice = FLibAPI.getNBTCap((Entity) inputs[0]).getCompound("faeruncharacters").getCompoundTag("CC").getString("Voice");
        return ALL_VOICE_SETS.get(voice).get("" + inputs[1]).toString();
    }
}
