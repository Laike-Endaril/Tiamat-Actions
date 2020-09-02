package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.other.GUIDarkenedBackground;
import com.fantasticsource.mctools.gui.element.text.GUILabeledBoolean;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.mctools.gui.screen.TextSelectionGUI;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.component.CBoolean;
import com.fantasticsource.tools.component.CStringUTF8;
import com.fantasticsource.tools.datastructures.Pair;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodePlaySoundAtEntityPosition extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/play_sound_at_entity_position.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();
    protected static String[] soundCategoryNames;

    static
    {
        REQUIRED_INPUTS.put("sound", Object.class);
        REQUIRED_INPUTS.put("entity", Entity.class);
        REQUIRED_INPUTS.put("maxDistance", Object.class);
        REQUIRED_INPUTS.put("volume", Object.class);
        REQUIRED_INPUTS.put("pitch", Object.class);

        soundCategoryNames = new String[SoundCategory.values().length];
        int i = 0;
        for (SoundCategory soundCategory : SoundCategory.values()) soundCategoryNames[i++] = soundCategory.name();
    }

    protected String soundCategoryName = SoundCategory.MASTER.name();
    protected boolean attenuation = true;

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodePlaySoundAtEntityPosition()
    {
        super();
    }

    public CNodePlaySoundAtEntityPosition(String actionName, String event, int x, int y)
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
        return "Play a sound at a given entity's current position";
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
        MCTools.playSimpleSoundAtEntityPosition(
                new ResourceLocation("" + inputs[0]),
                (Entity) inputs[1],
                Double.parseDouble("" + inputs[2]),
                attenuation ? 2 : 0,
                Float.parseFloat("" + inputs[3]),
                Float.parseFloat("" + inputs[4]),
                SoundCategory.getByName(soundCategoryName)
        );

        return null;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen showNodeEditGUI()
    {
        return new PlaySoundNodeGUI(this);
    }


    @Override
    public CNodePlaySoundAtEntityPosition write(ByteBuf buf)
    {
        super.write(buf);

        buf.writeBoolean(attenuation);
        ByteBufUtils.writeUTF8String(buf, soundCategoryName);

        return this;
    }

    @Override
    public CNodePlaySoundAtEntityPosition read(ByteBuf buf)
    {
        super.read(buf);

        attenuation = buf.readBoolean();
        soundCategoryName = ByteBufUtils.readUTF8String(buf);

        return this;
    }

    @Override
    public CNodePlaySoundAtEntityPosition save(OutputStream stream)
    {
        super.save(stream);

        new CBoolean().set(attenuation).save(stream);
        new CStringUTF8().set(soundCategoryName).save(stream);

        return this;
    }

    @Override
    public CNodePlaySoundAtEntityPosition load(InputStream stream)
    {
        super.load(stream);

        attenuation = new CBoolean().load(stream).value;
        soundCategoryName = new CStringUTF8().load(stream).value;

        return this;
    }


    @SideOnly(Side.CLIENT)
    public static class PlaySoundNodeGUI extends GUIScreen
    {
        protected PlaySoundNodeGUI(CNodePlaySoundAtEntityPosition node)
        {
            show(node);
        }

        @Override
        public String title()
        {
            return "Play Sound Node";
        }

        protected void show(CNodePlaySoundAtEntityPosition node)
        {
            show();


            //Background
            root.add(new GUIDarkenedBackground(this));


            //Header
            GUINavbar navbar = new GUINavbar(this);
            root.add(navbar);


            //Data
            GUIText soundCategoryLabel = new GUIText(this, "Sound Category: ");
            GUIText soundCategory = new GUIText(this, node.soundCategoryName);

            soundCategoryLabel.linkMouseActivity(soundCategory);
            soundCategory.linkMouseActivity(soundCategoryLabel);

            root.addAll(
                    soundCategoryLabel.addClickActions(soundCategory::click),
                    soundCategory.addClickActions(() ->
                    {
                        TextSelectionGUI gui = new TextSelectionGUI(soundCategory, "Select Sound Category", soundCategoryNames);
                        gui.addOnClosedActions(() -> node.soundCategoryName = soundCategory.getText());
                    })
            );

            GUILabeledBoolean attenuation = new GUILabeledBoolean(this, "Attenuation: ", node.attenuation);
            root.add(attenuation.addClickActions(() -> node.attenuation = attenuation.getValue()));
        }
    }
}
