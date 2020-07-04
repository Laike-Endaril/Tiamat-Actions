package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUIDarkenedBackground;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.component.CInt;
import com.fantasticsource.tools.datastructures.Color;
import com.fantasticsource.tools.datastructures.Pair;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeComparison extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/comparison.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("value1", Object.class);
        REQUIRED_INPUTS.put("value2", Object.class);
    }

    public static final int
            TYPE_EQUALS = 0,
            TYPE_LESS_THAN = 1,
            TYPE_GREATER_THAN = 2,
            TYPE_LESS_THAN_OR_EQUAL = 3,
            TYPE_GREATER_THAN_OR_EQUAL = 4;

    protected int type = TYPE_EQUALS;

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeComparison()
    {
        super();
    }

    public CNodeComparison(String actionName, String event, int x, int y)
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
        return "Is value1 " + getSymbol() + " value2?";
    }

    protected String getSymbol()
    {
        switch (type)
        {
            case TYPE_EQUALS:
                return "==";

            case TYPE_LESS_THAN:
                return "<";

            case TYPE_GREATER_THAN:
                return ">";

            case TYPE_LESS_THAN_OR_EQUAL:
                return "<=";

            case TYPE_GREATER_THAN_OR_EQUAL:
                return ">=";

            default:
                return TextFormatting.RED + "ERROR" + TextFormatting.RESET;
        }
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
    public Object execute(CAction mainAction, Object... inputs)
    {
        String s1 = "" + inputs[0], s2 = "" + inputs[1];
        int result = s1.compareTo(s2);

        switch (type)
        {
            case TYPE_EQUALS:
                return result == 0;

            case TYPE_LESS_THAN:
                return result < 0;

            case TYPE_GREATER_THAN:
                return result > 0;

            case TYPE_LESS_THAN_OR_EQUAL:
                return result <= 0;

            case TYPE_GREATER_THAN_OR_EQUAL:
                return result >= 0;

            default:
                throw new IllegalStateException("Invalid comparison type: " + type);
        }
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen showNodeEditGUI()
    {
        return new ComparisonNodeGUI(this);
    }


    @Override
    public CNodeComparison write(ByteBuf buf)
    {
        super.write(buf);

        buf.writeInt(type);

        return this;
    }

    @Override
    public CNodeComparison read(ByteBuf buf)
    {
        super.read(buf);

        type = buf.readInt();

        return this;
    }

    @Override
    public CNodeComparison save(OutputStream stream)
    {
        super.save(stream);

        new CInt().set(type).save(stream);

        return this;
    }

    @Override
    public CNodeComparison load(InputStream stream)
    {
        super.load(stream);

        type = new CInt().load(stream).value;

        return this;
    }


    @SideOnly(Side.CLIENT)
    public static class ComparisonNodeGUI extends GUIScreen
    {
        protected static final Color[]
                WHITE = new Color[]{GUIScreen.getIdleColor(Color.WHITE), GUIScreen.getHoverColor(Color.WHITE), Color.WHITE},
                PURPLE = new Color[]{GUIScreen.getIdleColor(Color.PURPLE), GUIScreen.getHoverColor(Color.PURPLE), Color.PURPLE};

        protected ComparisonNodeGUI(CNodeComparison node)
        {
            show(node);
        }

        @Override
        public String title()
        {
            return "Comparison Node";
        }

        protected void show(CNodeComparison node)
        {
            show();


            //Background
            root.add(new GUIDarkenedBackground(this));


            //Header
            GUINavbar navbar = new GUINavbar(this);
            root.add(navbar);


            //Data
            Color[] color = node.type == TYPE_EQUALS ? PURPLE : WHITE;
            root.add(new GUIText(this, "Equal (==)", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_EQUALS;
                close();
            }));

            color = node.type == TYPE_LESS_THAN ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Less Than (<)", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_LESS_THAN;
                close();
            }));

            color = node.type == TYPE_GREATER_THAN ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Greater Than (>)", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_GREATER_THAN;
                close();
            }));

            color = node.type == TYPE_LESS_THAN_OR_EQUAL ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Less Than or Equal (<=)", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_LESS_THAN_OR_EQUAL;
                close();
            }));

            color = node.type == TYPE_GREATER_THAN_OR_EQUAL ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Greater Than or Equal (>=)", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_GREATER_THAN_OR_EQUAL;
                close();
            }));
        }
    }
}
