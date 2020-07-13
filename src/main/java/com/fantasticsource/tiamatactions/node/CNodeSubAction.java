package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.other.GUIDarkenedBackground;
import com.fantasticsource.mctools.gui.element.text.GUILabeledBoolean;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
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

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeSubAction extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/sub_action.png");
    protected static final Pair<String, Class> OPTIONAL_INPUTS = new Pair<>("forEach", Object.class);
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("actionName", Object.class);
    }

    protected boolean expandArrays = true;

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeSubAction()
    {
        super();
    }

    public CNodeSubAction(String actionName, String event, int x, int y)
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
        return "Run another action as part of this action";
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
        return Object.class;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        CAction newSubAction = CAction.ALL_ACTIONS.get(inputs[0]);
        if (newSubAction == null || newSubAction.tickEndpointNodes.size() > 0) throw new IllegalArgumentException("Cannot run actions with tick tasks as sub-actions!");

        if (inputs.length == 1)
        {
            return newSubAction.queue(mainAction.source, null, mainAction.mainAction);
        }


        if (inputs.length == 2)
        {
            Object arg = inputs[1];
            if (arg != null && arg.getClass().isArray())
            {
                for (Object o : (Object[]) arg) newSubAction.queue(mainAction.source, null, mainAction.mainAction, o);
                return null;
            }

            if (arg instanceof Iterable)
            {
                for (Object o : (Iterable) arg) newSubAction.queue(mainAction.source, null, mainAction.mainAction, o);
                return null;
            }

            return newSubAction.queue(mainAction.source, null, mainAction.mainAction, inputs[1]);
        }


        for (int i = 1; i < inputs.length; i++)
        {
            Object arg = inputs[i];
            if (arg.getClass().isArray())
            {
                for (Object o : (Object[]) arg) newSubAction.queue(mainAction.source, null, mainAction.mainAction, o);
                continue;
            }

            if (arg instanceof Iterable)
            {
                for (Object o : (Iterable) arg) newSubAction.queue(mainAction.source, null, mainAction.mainAction, o);
                return null;
            }

            newSubAction.queue(mainAction.source, null, mainAction.mainAction, inputs[i]);
        }
        return null;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen showNodeEditGUI()
    {
        return new SubActionNodeGUI(this);
    }


    @Override
    public CNodeSubAction write(ByteBuf buf)
    {
        super.write(buf);

        buf.writeBoolean(expandArrays);

        return this;
    }

    @Override
    public CNodeSubAction read(ByteBuf buf)
    {
        super.read(buf);

        expandArrays = buf.readBoolean();

        return this;
    }

    @Override
    public CNodeSubAction save(OutputStream stream)
    {
        super.save(stream);

        new CBoolean().set(expandArrays).save(stream);

        return this;
    }

    @Override
    public CNodeSubAction load(InputStream stream)
    {
        super.load(stream);

        expandArrays = new CBoolean().load(stream).value;

        return this;
    }


    @SideOnly(Side.CLIENT)
    public static class SubActionNodeGUI extends GUIScreen
    {
        protected SubActionNodeGUI(CNodeSubAction node)
        {
            show(node);
        }

        @Override
        public String title()
        {
            return "Sub-Action Node";
        }

        protected void show(CNodeSubAction node)
        {
            show();


            //Background
            root.add(new GUIDarkenedBackground(this));


            //Header
            GUINavbar navbar = new GUINavbar(this);
            root.add(navbar);


            //Data
            GUILabeledBoolean expandArrays = new GUILabeledBoolean(this, "Expand Arrays: ", node.expandArrays);
            root.add(expandArrays.addClickActions(() -> node.expandArrays = expandArrays.getValue()));
        }
    }
}
