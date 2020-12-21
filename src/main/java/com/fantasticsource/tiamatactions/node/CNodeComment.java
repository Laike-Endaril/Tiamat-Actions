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

public class CNodeComment extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/comment.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeComment()
    {
        super();
    }

    public CNodeComment(String actionName, String event, int x, int y)
    {
        super(actionName, event, x, y);
    }


    public String comment = "";


    @Override
    public ResourceLocation getTexture()
    {
        return TEXTURE;
    }

    @Override
    public String getDescription()
    {
        return comment;
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
        return null;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen showNodeEditGUI()
    {
        return new CommentNodeGUI(this);
    }


    @Override
    public CNodeComment write(ByteBuf buf)
    {
        super.write(buf);

        ByteBufUtils.writeUTF8String(buf, comment);

        return this;
    }

    @Override
    public CNodeComment read(ByteBuf buf)
    {
        super.read(buf);

        comment = ByteBufUtils.readUTF8String(buf);

        return this;
    }

    @Override
    public CNodeComment save(OutputStream stream)
    {
        super.save(stream);

        new CStringUTF8().set(comment).save(stream);

        return this;
    }

    @Override
    public CNodeComment load(InputStream stream)
    {
        super.load(stream);

        comment = new CStringUTF8().load(stream).value;

        return this;
    }


    @SideOnly(Side.CLIENT)
    public static class CommentNodeGUI extends GUIScreen
    {
        protected CommentNodeGUI(CNodeComment node)
        {
            show(node);
        }

        @Override
        public String title()
        {
            return "Comment Node";
        }

        protected void show(CNodeComment node)
        {
            show();


            //Background
            root.add(new GUIDarkenedBackground(this));


            //Header
            GUINavbar navbar = new GUINavbar(this);
            root.add(navbar);


            //Data
            GUILabeledTextInput string = new GUILabeledTextInput(this, "Comment: ", node.comment, FilterNone.INSTANCE);
            string.addEditActions(() -> node.comment = string.getText());
            string.input.setActive(true);
            root.addAll(
                    new GUITextSpacer(this),
                    new GUITextSpacer(this, true),
                    string
            );
        }
    }
}
