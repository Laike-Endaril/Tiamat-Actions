package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.other.GUIDarkenedBackground;
import com.fantasticsource.mctools.gui.element.text.GUILabeledTextInput;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUITextSpacer;
import com.fantasticsource.mctools.gui.element.text.filter.FilterNone;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.component.CStringUTF8;
import com.fantasticsource.tools.datastructures.Pair;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeString extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/string.png");
    protected static final Pair<String, Class> OPTIONAL_INPUTS = new Pair<>("args", Object.class);
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeString()
    {
        super();
    }

    public CNodeString(String actionName, String event, int x, int y)
    {
        super(actionName, event, x, y);
    }


    public String string = "";


    @Override
    public ResourceLocation getTexture()
    {
        return TEXTURE;
    }

    @Override
    public String getDescription()
    {
        return "Output string: " + string;
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
        String result = string.replaceAll("@p|@P", mainAction.source.getName());

        for (int i = 0; i < inputs.length; i++)
        {
            result = result.replaceAll("@" + (i + 1), "" + inputs[i]);
        }

        return result;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen showNodeEditGUI()
    {
        return new StringNodeGUI(this);
    }


    @Override
    public CNodeString write(ByteBuf buf)
    {
        super.write(buf);

        ByteBufUtils.writeUTF8String(buf, string);

        return this;
    }

    @Override
    public CNodeString read(ByteBuf buf)
    {
        super.read(buf);

        string = ByteBufUtils.readUTF8String(buf);

        return this;
    }

    @Override
    public CNodeString save(OutputStream stream)
    {
        super.save(stream);

        new CStringUTF8().set(string).save(stream);

        return this;
    }

    @Override
    public CNodeString load(InputStream stream)
    {
        super.load(stream);

        string = new CStringUTF8().load(stream).value;

        return this;
    }


    @SideOnly(Side.CLIENT)
    public static class StringNodeGUI extends GUIScreen
    {
        protected StringNodeGUI(CNodeString node)
        {
            show(node);
        }

        @Override
        public String title()
        {
            return "String Node";
        }

        protected void show(CNodeString node)
        {
            show();


            //Background
            root.add(new GUIDarkenedBackground(this));


            //Header
            GUINavbar navbar = new GUINavbar(this);
            root.add(navbar);


            //Data
            GUILabeledTextInput string = new GUILabeledTextInput(this, "String: ", node.string, FilterNone.INSTANCE);
            string.addEditActions(() -> node.string = string.getText());
            string.input.setActive(true);
            root.addAll(
                    new GUITextSpacer(this),
                    new GUITextSpacer(this, true),
                    string
            );
        }
    }
}
