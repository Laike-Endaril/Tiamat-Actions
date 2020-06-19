package com.fantasticsource.tiamatactions.node.staticoutput;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.other.GUIDarkenedBackground;
import com.fantasticsource.mctools.gui.element.text.GUILabeledTextInput;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUITextSpacer;
import com.fantasticsource.mctools.gui.element.text.filter.FilterFloat;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.component.CDouble;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.InputStream;
import java.io.OutputStream;

public class CNodeNumber extends CNode
{
    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeNumber()
    {
        super();
    }

    public CNodeNumber(String actionName, String event, int x, int y)
    {
        super(actionName, event, x, y);
    }


    public double number;


    @Override
    public String getDescription()
    {
        return "Output number: " + number;
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
        return CAction.ALL_ACTIONS.get(actionName).source;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen showNodeEditGUI()
    {
        return new NumberNodeGUI(this);
    }


    @Override
    public CNodeNumber write(ByteBuf buf)
    {
        super.write(buf);

        buf.writeDouble(number);

        return this;
    }

    @Override
    public CNodeNumber read(ByteBuf buf)
    {
        super.read(buf);

        number = buf.readDouble();

        return this;
    }

    @Override
    public CNodeNumber save(OutputStream stream)
    {
        super.save(stream);

        new CDouble().set(number).save(stream);

        return this;
    }

    @Override
    public CNodeNumber load(InputStream stream)
    {
        super.load(stream);

        number = new CDouble().load(stream).value;

        return this;
    }


    public static class NumberNodeGUI extends GUIScreen
    {
        protected NumberNodeGUI(CNodeNumber node)
        {
            show(node);
        }

        @Override
        public String title()
        {
            return "Number Node";
        }

        protected void show(CNodeNumber node)
        {
            show();


            //Background
            root.add(new GUIDarkenedBackground(this));


            //Header
            GUINavbar navbar = new GUINavbar(this);
            root.add(navbar);


            //Data
            GUILabeledTextInput number = new GUILabeledTextInput(this, "Number: ", "" + node.number, FilterFloat.INSTANCE);
            number.addEditActions(() ->
            {
                if (number.valid()) node.number = FilterFloat.INSTANCE.parse(number.getText());
            });
            root.addAll(
                    new GUITextSpacer(this),
                    new GUITextSpacer(this, true),
                    number
            );
        }
    }
}
