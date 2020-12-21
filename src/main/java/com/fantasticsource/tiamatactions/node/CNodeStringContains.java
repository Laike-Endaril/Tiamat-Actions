package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUIDarkenedBackground;
import com.fantasticsource.mctools.gui.element.text.GUILabeledTextInput;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.mctools.gui.element.text.GUITextSpacer;
import com.fantasticsource.mctools.gui.element.text.filter.FilterNone;
import com.fantasticsource.mctools.gui.screen.TextSelectionGUI;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.component.CStringUTF8;
import com.fantasticsource.tools.datastructures.Color;
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

public class CNodeStringContains extends CNode
{
    protected static final String MODES[] = new String[]{"Normal", "Regex"};
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/string_contains.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("string", Object.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeStringContains()
    {
        super();
    }

    public CNodeStringContains(String actionName, String event, int x, int y)
    {
        super(actionName, event, x, y);
    }


    public String find = "";
    public String mode = "Normal";


    @Override
    public ResourceLocation getTexture()
    {
        return TEXTURE;
    }

    @Override
    public String getDescription()
    {
        return "Return whether an input string contains " + find;
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
        String s = "" + inputs[0];
        switch (mode)
        {
            case "Regex":
                return !s.equals(s.replaceFirst(find, ""));

            default:
                return s.contains(find);
        }
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen showNodeEditGUI()
    {
        return new StringContainsNodeGUI(this);
    }


    @Override
    public CNodeStringContains write(ByteBuf buf)
    {
        super.write(buf);

        ByteBufUtils.writeUTF8String(buf, find);
        ByteBufUtils.writeUTF8String(buf, mode);

        return this;
    }

    @Override
    public CNodeStringContains read(ByteBuf buf)
    {
        super.read(buf);

        find = ByteBufUtils.readUTF8String(buf);
        mode = ByteBufUtils.readUTF8String(buf);

        return this;
    }

    @Override
    public CNodeStringContains save(OutputStream stream)
    {
        super.save(stream);

        new CStringUTF8().set(find).save(stream).set(mode).save(stream);

        return this;
    }

    @Override
    public CNodeStringContains load(InputStream stream)
    {
        super.load(stream);

        find = new CStringUTF8().load(stream).value;
        mode = new CStringUTF8().load(stream).value;

        return this;
    }


    @SideOnly(Side.CLIENT)
    public static class StringContainsNodeGUI extends GUIScreen
    {
        protected StringContainsNodeGUI(CNodeStringContains node)
        {
            show(node);
        }

        @Override
        public String title()
        {
            return "String Contains Node";
        }

        protected void show(CNodeStringContains node)
        {
            show();


            //Background
            root.add(new GUIDarkenedBackground(this));


            //Header
            GUINavbar navbar = new GUINavbar(this);
            root.add(navbar);


            //Data
            GUILabeledTextInput string = new GUILabeledTextInput(this, "String: ", node.find, FilterNone.INSTANCE);
            string.addEditActions(() -> node.find = string.getText());
            string.input.setActive(true);
            root.addAll(
                    new GUITextSpacer(this),
                    string
            );

            GUIText modeLabel = new GUIText(this, "Mode: ", getIdleColor(Color.WHITE), getHoverColor(Color.WHITE), Color.WHITE);
            GUIText mode = new GUIText(this, node.mode, getIdleColor(Color.WHITE), getHoverColor(Color.WHITE), Color.WHITE);

            modeLabel.linkMouseActivity(mode);
            mode.linkMouseActivity(modeLabel);

            root.addAll(
                    new GUIElement(this, 1, 0),
                    modeLabel.addClickActions(mode::click),
                    mode.addClickActions(() ->
                    {
                        TextSelectionGUI gui = new TextSelectionGUI(mode, "Select Mode", MODES);
                        gui.addOnClosedActions(() -> node.mode = mode.getText());
                    })
            );
        }
    }
}
