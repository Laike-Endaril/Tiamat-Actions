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

public class CNodeBoolean extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/boolean.png");
    protected static final Pair<String, Class> OPTIONAL_INPUTS = new Pair<>("moreInputs", Boolean.class);
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS_TF = new LinkedHashMap<>(), REQUIRED_INPUTS_OTHER = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS_OTHER.put("input1", Boolean.class);
    }

    public static final int
            TYPE_OR = 0,
            TYPE_NOR = 1,
            TYPE_AND = 2,
            TYPE_NAND = 3,
            TYPE_XOR = 4,
            TYPE_XNOR = 5,
            TYPE_TRUE = 6,
            TYPE_FALSE = 7;

    protected int type = TYPE_OR;

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeBoolean()
    {
        super();
    }

    public CNodeBoolean(String actionName, String event, int x, int y)
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
        switch (type)
        {
            case TYPE_OR:
                return "OR";

            case TYPE_NOR:
                return "NOR";

            case TYPE_AND:
                return "AND";

            case TYPE_NAND:
                return "NAND";

            case TYPE_XOR:
                return "XOR";

            case TYPE_XNOR:
                return "XNOR";

            case TYPE_TRUE:
                return "TRUE";

            case TYPE_FALSE:
                return "FALSE";

            default:
                return TextFormatting.RED + "ERROR" + TextFormatting.RESET;
        }
    }


    @Override
    public LinkedHashMap<String, Class> getRequiredInputs()
    {
        return type == TYPE_TRUE || type == TYPE_FALSE ? REQUIRED_INPUTS_TF : REQUIRED_INPUTS_OTHER;
    }

    @Override
    public Pair<String, Class> getOptionalInputs()
    {
        return OPTIONAL_INPUTS;
    }

    @Override
    public Class outputType()
    {
        return Boolean.class;
    }


    @Override
    public Object execute(CAction mainAction, Object... inputs)
    {
        switch (type)
        {
            case TYPE_OR:
                for (Object input : inputs) if ((boolean) input) return true;
                return false;

            case TYPE_NOR:
                for (Object input : inputs) if ((boolean) input) return false;
                return true;

            case TYPE_AND:
                for (Object input : inputs) if (!(boolean) input) return false;
                return true;

            case TYPE_NAND:
                for (Object input : inputs) if (!(boolean) input) return true;
                return false;

            case TYPE_XOR:
            {
                int count = 0;
                for (Object input : inputs) if ((boolean) input) count++;
                return count % 2 == 1;
            }

            case TYPE_XNOR:
            {
                int count = 0;
                for (Object input : inputs) if ((boolean) input) count++;
                return count % 2 == 0;
            }

            case TYPE_TRUE:
                return true;

            case TYPE_FALSE:
                return false;

            default:
                throw new IllegalStateException("Invalid boolean type: " + type);
        }
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen showNodeEditGUI()
    {
        return new BooleanNodeGUI(this);
    }


    @Override
    public CNodeBoolean write(ByteBuf buf)
    {
        super.write(buf);

        buf.writeInt(type);

        return this;
    }

    @Override
    public CNodeBoolean read(ByteBuf buf)
    {
        super.read(buf);

        type = buf.readInt();

        return this;
    }

    @Override
    public CNodeBoolean save(OutputStream stream)
    {
        super.save(stream);

        new CInt().set(type).save(stream);

        return this;
    }

    @Override
    public CNodeBoolean load(InputStream stream)
    {
        super.load(stream);

        type = new CInt().load(stream).value;

        return this;
    }


    @SideOnly(Side.CLIENT)
    public static class BooleanNodeGUI extends GUIScreen
    {
        protected static final Color[]
                WHITE = new Color[]{GUIScreen.getIdleColor(Color.WHITE), GUIScreen.getHoverColor(Color.WHITE), Color.WHITE},
                PURPLE = new Color[]{GUIScreen.getIdleColor(Color.PURPLE), GUIScreen.getHoverColor(Color.PURPLE), Color.PURPLE};

        protected BooleanNodeGUI(CNodeBoolean node)
        {
            show(node);
        }

        @Override
        public String title()
        {
            return "Boolean Node";
        }

        protected void show(CNodeBoolean node)
        {
            show();


            //Background
            root.add(new GUIDarkenedBackground(this));


            //Header
            GUINavbar navbar = new GUINavbar(this);
            root.add(navbar);


            //Data
            Color[] color = node.type == TYPE_OR ? PURPLE : WHITE;
            root.add(new GUIText(this, "OR", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_OR;
                close();
            }));

            color = node.type == TYPE_NOR ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "NOR", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_NOR;
                close();
            }));

            color = node.type == TYPE_AND ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "AND", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_AND;
                close();
            }));

            color = node.type == TYPE_NAND ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "NAND", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_NAND;
                close();
            }));

            color = node.type == TYPE_XOR ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "XOR", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_XOR;
                close();
            }));

            color = node.type == TYPE_XNOR ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "XNOR", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_XNOR;
                close();
            }));

            color = node.type == TYPE_TRUE ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "TRUE", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TRUE;
                close();
            }));

            color = node.type == TYPE_FALSE ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "FALSE", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_FALSE;
                close();
            }));
        }
    }
}
