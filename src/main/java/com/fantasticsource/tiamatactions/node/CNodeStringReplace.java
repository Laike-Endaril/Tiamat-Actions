package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUIDarkenedBackground;
import com.fantasticsource.mctools.gui.element.text.*;
import com.fantasticsource.mctools.gui.element.text.filter.FilterNone;
import com.fantasticsource.mctools.gui.screen.TextSelectionGUI;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.component.CBoolean;
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
import java.util.regex.Pattern;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeStringReplace extends CNode
{
    protected static final String MODES[] = new String[]{"Normal", "Regex"};
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/string_replace.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("string", Object.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeStringReplace()
    {
        super();
    }

    public CNodeStringReplace(String actionName, String event, int x, int y)
    {
        super(actionName, event, x, y);
    }


    public String find = "", replacement = "", mode = "Normal";
    public boolean all = true;


    @Override
    public ResourceLocation getTexture()
    {
        return TEXTURE;
    }

    @Override
    public String getDescription()
    {
        return "Replace " + find + " with " + replacement;
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
        String s = "" + inputs[0];
        switch (mode)
        {
            case "Regex":
                if (all) return s.replaceAll(find, replacement);
                return s.replaceFirst(find, replacement);

            default:
                if (all) return s.replace(find, replacement);
                return s.replaceFirst(Pattern.quote(find), Pattern.quote(replacement));
        }
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen showNodeEditGUI()
    {
        return new StringReplaceNodeGUI(this);
    }


    @Override
    public CNodeStringReplace write(ByteBuf buf)
    {
        super.write(buf);

        ByteBufUtils.writeUTF8String(buf, find);
        ByteBufUtils.writeUTF8String(buf, replacement);
        ByteBufUtils.writeUTF8String(buf, mode);
        buf.writeBoolean(all);

        return this;
    }

    @Override
    public CNodeStringReplace read(ByteBuf buf)
    {
        super.read(buf);

        find = ByteBufUtils.readUTF8String(buf);
        replacement = ByteBufUtils.readUTF8String(buf);
        mode = ByteBufUtils.readUTF8String(buf);
        all = buf.readBoolean();

        return this;
    }

    @Override
    public CNodeStringReplace save(OutputStream stream)
    {
        super.save(stream);

        new CStringUTF8().set(find).save(stream).set(replacement).save(stream).set(mode).save(stream);
        new CBoolean().set(all).save(stream);

        return this;
    }

    @Override
    public CNodeStringReplace load(InputStream stream)
    {
        super.load(stream);

        find = new CStringUTF8().load(stream).value;
        replacement = new CStringUTF8().load(stream).value;
        mode = new CStringUTF8().load(stream).value;
        all = new CBoolean().load(stream).value;

        return this;
    }


    @SideOnly(Side.CLIENT)
    public static class StringReplaceNodeGUI extends GUIScreen
    {
        protected StringReplaceNodeGUI(CNodeStringReplace node)
        {
            show(node);
        }

        @Override
        public String title()
        {
            return "String Replacement Node";
        }

        protected void show(CNodeStringReplace node)
        {
            show();


            //Background
            root.add(new GUIDarkenedBackground(this));


            //Header
            GUINavbar navbar = new GUINavbar(this);
            root.add(navbar);


            //Data
            GUILabeledTextInput string = new GUILabeledTextInput(this, "Find: ", node.find, FilterNone.INSTANCE);
            string.addEditActions(() -> node.find = string.getText());
            root.addAll(
                    new GUITextSpacer(this),
                    string
            );

            GUILabeledTextInput replacement = new GUILabeledTextInput(this, "Replace with: ", node.replacement, FilterNone.INSTANCE);
            replacement.addEditActions(() -> node.replacement = replacement.getText());
            root.addAll(
                    new GUIElement(this, 1, 0),
                    replacement
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

            GUILabeledBoolean all = new GUILabeledBoolean(this, "Replace all: ", node.all);
            root.addAll(
                    new GUIElement(this, 1, 0),
                    all.addClickActions(() -> node.all = all.getValue())
            );
        }
    }
}
