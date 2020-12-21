package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.other.GUIDarkenedBackground;
import com.fantasticsource.mctools.gui.element.text.GUILabeledTextInput;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUITextSpacer;
import com.fantasticsource.mctools.gui.element.text.filter.FilterFloat;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.component.CDouble;
import com.fantasticsource.tools.datastructures.Pair;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeVector extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/vector.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeVector()
    {
        super();
    }

    public CNodeVector(String actionName, String event, int x, int y)
    {
        super(actionName, event, x, y);
    }


    public Vec3d vector = new Vec3d(0, 0, 0);


    @Override
    public ResourceLocation getTexture()
    {
        return TEXTURE;
    }

    @Override
    public String getDescription()
    {
        return "Output vector: " + vector;
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
        return Vec3d.class;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        return vector;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen showNodeEditGUI()
    {
        return new VectorNodeGUI(this);
    }


    @Override
    public CNodeVector write(ByteBuf buf)
    {
        super.write(buf);

        buf.writeDouble(vector.x);
        buf.writeDouble(vector.y);
        buf.writeDouble(vector.z);

        return this;
    }

    @Override
    public CNodeVector read(ByteBuf buf)
    {
        super.read(buf);

        vector = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());

        return this;
    }

    @Override
    public CNodeVector save(OutputStream stream)
    {
        super.save(stream);

        new CDouble().set(vector.x).save(stream).set(vector.y).save(stream).set(vector.z).save(stream);

        return this;
    }

    @Override
    public CNodeVector load(InputStream stream)
    {
        super.load(stream);

        CDouble cd = new CDouble();
        vector = new Vec3d(cd.load(stream).value, cd.load(stream).value, cd.load(stream).value);

        return this;
    }


    @SideOnly(Side.CLIENT)
    public static class VectorNodeGUI extends GUIScreen
    {
        protected VectorNodeGUI(CNodeVector node)
        {
            show(node);
        }

        @Override
        public String title()
        {
            return "Vector Node";
        }

        protected void show(CNodeVector node)
        {
            show();


            //Background
            root.add(new GUIDarkenedBackground(this));


            //Header
            GUINavbar navbar = new GUINavbar(this);
            root.add(navbar);


            //Data
            GUILabeledTextInput x = new GUILabeledTextInput(this, "X: ", "" + node.vector.x, FilterFloat.INSTANCE);
            x.addEditActions(() ->
            {
                if (x.valid()) node.vector = new Vec3d(Double.parseDouble(x.getText()), node.vector.y, node.vector.z);
            });
            x.input.setActive(true);
            GUILabeledTextInput y = new GUILabeledTextInput(this, "Y: ", "" + node.vector.y, FilterFloat.INSTANCE);
            y.addEditActions(() ->
            {
                if (y.valid()) node.vector = new Vec3d(node.vector.x, Double.parseDouble(y.getText()), node.vector.z);
            });
            GUILabeledTextInput z = new GUILabeledTextInput(this, "Z: ", "" + node.vector.z, FilterFloat.INSTANCE);
            z.addEditActions(() ->
            {
                if (z.valid()) node.vector = new Vec3d(node.vector.x, node.vector.y, Double.parseDouble(z.getText()));
            });
            root.addAll(
                    new GUITextSpacer(this),
                    x,
                    new GUITextSpacer(this),
                    y,
                    new GUITextSpacer(this),
                    z
            );
        }
    }
}
