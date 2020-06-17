package com.fantasticsource.tiamatactions.node.staticoutput;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.other.GUIDarkenedBackground;
import com.fantasticsource.mctools.gui.element.text.GUILabeledTextInput;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUITextSpacer;
import com.fantasticsource.mctools.gui.element.text.filter.FilterNone;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.component.CStringUTF8;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.InputStream;
import java.io.OutputStream;

public class CNodeString extends CNode
{
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
    public String getDescription()
    {
        return "Output string: " + string;
    }


    @Override
    public Class[] requiredInputTypes()
    {
        return new Class[0];
    }

    @Override
    public Class arrayInputType()
    {
        return null;
    }

    @Override
    public Class outputType()
    {
        return String.class;
    }


    @Override
    public Object execute(CAction parentAction, Object... inputs)
    {
        return string;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen getNodeEditGUI()
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
            root.addAll(
                    new GUITextSpacer(this),
                    new GUITextSpacer(this, true),
                    string
            );
        }
    }
}
