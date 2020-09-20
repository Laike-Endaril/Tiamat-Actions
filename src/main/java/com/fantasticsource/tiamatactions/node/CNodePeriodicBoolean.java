package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.ClientTickTimer;
import com.fantasticsource.mctools.ServerTickTimer;
import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.other.GUIDarkenedBackground;
import com.fantasticsource.mctools.gui.element.text.GUILabeledTextInput;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUITextSpacer;
import com.fantasticsource.mctools.gui.element.text.filter.FilterRangedInt;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.component.CInt;
import com.fantasticsource.tools.datastructures.Pair;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodePeriodicBoolean extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/periodic_boolean.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();
    protected static final FilterRangedInt FILTER_GREATER_THAN_0 = FilterRangedInt.get(1, Integer.MAX_VALUE);

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodePeriodicBoolean()
    {
        super();
    }

    public CNodePeriodicBoolean(String actionName, String event, int x, int y)
    {
        super(actionName, event, x, y);
    }


    public int delay = 20;


    @Override
    public ResourceLocation getTexture()
    {
        return TEXTURE;
    }

    @Override
    public String getDescription()
    {
        return "Return true once every " + delay + " ticks";
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
        return Boolean.class;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        Entity source = mainAction.source;
        if (!source.world.isRemote) return Tools.posMod((int) (ServerTickTimer.currentTick() - source.getUniqueID().getLeastSignificantBits()), delay) == 0;
        return Tools.posMod(ClientTickTimer.currentTick() - source.getUniqueID().getLeastSignificantBits(), delay) == 0;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen showNodeEditGUI()
    {
        return new PeriodicBooleanNodeGUI(this);
    }


    @Override
    public CNodePeriodicBoolean write(ByteBuf buf)
    {
        super.write(buf);

        buf.writeInt(delay);

        return this;
    }

    @Override
    public CNodePeriodicBoolean read(ByteBuf buf)
    {
        super.read(buf);

        delay = buf.readInt();

        return this;
    }

    @Override
    public CNodePeriodicBoolean save(OutputStream stream)
    {
        super.save(stream);

        new CInt().set(delay).save(stream);

        return this;
    }

    @Override
    public CNodePeriodicBoolean load(InputStream stream)
    {
        super.load(stream);

        delay = new CInt().load(stream).value;

        return this;
    }


    @SideOnly(Side.CLIENT)
    public static class PeriodicBooleanNodeGUI extends GUIScreen
    {
        protected PeriodicBooleanNodeGUI(CNodePeriodicBoolean node)
        {
            show(node);
        }

        @Override
        public String title()
        {
            return "Periodic Boolean Node";
        }

        protected void show(CNodePeriodicBoolean node)
        {
            show();


            //Background
            root.add(new GUIDarkenedBackground(this));


            //Header
            GUINavbar navbar = new GUINavbar(this);
            root.add(navbar);


            //Data
            GUILabeledTextInput delay = new GUILabeledTextInput(this, "Delay: ", "" + node.delay, FILTER_GREATER_THAN_0);
            delay.addEditActions(() -> node.delay = FILTER_GREATER_THAN_0.parse(delay.getText()));
            root.addAll(
                    new GUITextSpacer(this),
                    new GUITextSpacer(this, true),
                    delay
            );
        }
    }
}
