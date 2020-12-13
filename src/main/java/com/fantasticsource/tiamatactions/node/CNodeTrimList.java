package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.other.GUIDarkenedBackground;
import com.fantasticsource.mctools.gui.element.text.GUILabeledBoolean;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUITextSpacer;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.component.CBoolean;
import com.fantasticsource.tools.datastructures.Pair;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeTrimList extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/trim_list.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("list", List.class);
        REQUIRED_INPUTS.put("maxSize", Object.class);
    }

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeTrimList()
    {
        super();
    }

    public CNodeTrimList(String actionName, String event, int x, int y)
    {
        super(actionName, event, x, y);
    }


    public boolean fromEnd = true;


    @Override
    public ResourceLocation getTexture()
    {
        return TEXTURE;
    }

    @Override
    public String getDescription()
    {
        return "Trim a list to a maximum size";
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
        return List.class;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        List list = (List) inputs[0];
        int size = Integer.parseInt("" + inputs[1]);
        if (fromEnd)
        {
            while (list.size() > size) list.remove(list.size() - 1);
        }
        else
        {
            while (list.size() > size) list.remove(0);
        }
        return list;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen showNodeEditGUI()
    {
        return new TrimListNodeGUI(this);
    }


    @Override
    public CNodeTrimList write(ByteBuf buf)
    {
        super.write(buf);

        buf.writeBoolean(fromEnd);

        return this;
    }

    @Override
    public CNodeTrimList read(ByteBuf buf)
    {
        super.read(buf);

        fromEnd = buf.readBoolean();

        return this;
    }

    @Override
    public CNodeTrimList save(OutputStream stream)
    {
        super.save(stream);

        new CBoolean().set(fromEnd).save(stream);

        return this;
    }

    @Override
    public CNodeTrimList load(InputStream stream)
    {
        super.load(stream);

        fromEnd = new CBoolean().load(stream).value;

        return this;
    }


    @SideOnly(Side.CLIENT)
    public static class TrimListNodeGUI extends GUIScreen
    {
        protected TrimListNodeGUI(CNodeTrimList node)
        {
            show(node);
        }

        @Override
        public String title()
        {
            return "Trim List Node";
        }

        protected void show(CNodeTrimList node)
        {
            show();


            //Background
            root.add(new GUIDarkenedBackground(this));


            //Header
            GUINavbar navbar = new GUINavbar(this);
            root.add(navbar);


            //Data
            GUILabeledBoolean bool = new GUILabeledBoolean(this, "From End: ", node.fromEnd);
            bool.addEditActions(() -> node.fromEnd = bool.getValue());
            root.addAll(
                    new GUITextSpacer(this),
                    new GUITextSpacer(this, true),
                    bool
            );
        }
    }
}
