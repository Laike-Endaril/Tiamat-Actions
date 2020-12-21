package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.mctools.gui.screen.TextSelectionGUI;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.component.CStringUTF8;
import com.fantasticsource.tools.datastructures.Pair;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeSetNBTValue extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/set_nbt_value.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("nbt", NBTBase.class);
        REQUIRED_INPUTS.put("reference", String.class);
        REQUIRED_INPUTS.put("value", String.class);
    }

    protected String type = "Integer";

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeSetNBTValue()
    {
        super();
    }

    public CNodeSetNBTValue(String actionName, String event, int x, int y)
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
        return "Set a NBT tag value";
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
        NBTBase nbt = (NBTBase) inputs[0];
        String[] refs = Tools.fixedSplit("" + inputs[1], ":");

        for (int i = 0; i < refs.length; i++)
        {
            String ref = refs[i];

            if (i < refs.length - 1)
            {
                if (!((NBTTagCompound) nbt).hasKey(ref)) ((NBTTagCompound) nbt).setTag(ref, new NBTTagCompound());
                nbt = ((NBTTagCompound) nbt).getTag(ref);
            }
            else
            {
                switch (type)
                {
                    case "Integer":
                        ((NBTTagCompound) nbt).setInteger(ref, Integer.parseInt("" + inputs[2]));
                        break;

                    case "Double":
                        ((NBTTagCompound) nbt).setDouble(ref, Double.parseDouble("" + inputs[2]));
                        break;

                    case "Float":
                        ((NBTTagCompound) nbt).setFloat(ref, Float.parseFloat("" + inputs[2]));
                        break;

                    case "String":
                        ((NBTTagCompound) nbt).setString(ref, "" + inputs[2]);
                        break;

                    case "Short":
                        ((NBTTagCompound) nbt).setShort(ref, Short.parseShort("" + inputs[2]));
                        break;

                    case "Byte":
                        ((NBTTagCompound) nbt).setByte(ref, Byte.parseByte("" + inputs[2]));
                        break;

                    case "Long":
                        ((NBTTagCompound) nbt).setLong(ref, Long.parseLong("" + inputs[2]));
                        break;
                }
            }
        }

        return null;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen showNodeEditGUI()
    {
        GUIText guiText = new GUIText(new GUIScreen()
        {
            @Override
            public String title()
            {
                return null;
            }
        }, type);

        TextSelectionGUI gui = new TextSelectionGUI(guiText, "Set NBT Value Node", "Integer", "Double", "Float", "String", "Boolean", "Short", "Byte", "Long");
        gui.addOnClosedActions(() -> type = guiText.getText());
        return gui;
    }


    @Override
    public CNodeSetNBTValue write(ByteBuf buf)
    {
        super.write(buf);

        ByteBufUtils.writeUTF8String(buf, type);

        return this;
    }

    @Override
    public CNodeSetNBTValue read(ByteBuf buf)
    {
        super.read(buf);

        type = ByteBufUtils.readUTF8String(buf);

        return this;
    }

    @Override
    public CNodeSetNBTValue save(OutputStream stream)
    {
        super.save(stream);

        new CStringUTF8().set(type).save(stream);

        return this;
    }

    @Override
    public CNodeSetNBTValue load(InputStream stream)
    {
        super.load(stream);

        type = new CStringUTF8().load(stream).value;

        return this;
    }
}
