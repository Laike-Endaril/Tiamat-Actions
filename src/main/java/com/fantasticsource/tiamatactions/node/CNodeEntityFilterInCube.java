package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.EntityFilters;
import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.other.GUIDarkenedBackground;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.mctools.gui.screen.TextSelectionGUI;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.component.CInt;
import com.fantasticsource.tools.datastructures.Color;
import com.fantasticsource.tools.datastructures.Pair;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeEntityFilterInCube extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/entities_in_cube.png");
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("origin", Vec3d.class);
        REQUIRED_INPUTS.put("halfSize", Object.class);
        REQUIRED_INPUTS.put("entities", Entity[].class);
    }

    protected int inclusionMode = EntityFilters.INCLUSION_MODE_GEOMETRIC_CENTER;

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeEntityFilterInCube()
    {
        super();
    }

    public CNodeEntityFilterInCube(String actionName, String event, int x, int y)
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
        return "Only keep entities that collide with a given cube";
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
        return Entity[].class;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        ArrayList<Entity> entities = new ArrayList<>(Arrays.asList((Entity[]) inputs[2]));
        return EntityFilters.inCube((Vec3d) inputs[0], Double.parseDouble("" + inputs[1]), inclusionMode, entities).toArray(new Entity[0]);
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen showNodeEditGUI()
    {
        return new EntityFilterInCubeNodeGUI(this);
    }


    @Override
    public CNodeEntityFilterInCube write(ByteBuf buf)
    {
        super.write(buf);

        buf.writeInt(inclusionMode);

        return this;
    }

    @Override
    public CNodeEntityFilterInCube read(ByteBuf buf)
    {
        super.read(buf);

        inclusionMode = buf.readInt();

        return this;
    }

    @Override
    public CNodeEntityFilterInCube save(OutputStream stream)
    {
        super.save(stream);

        new CInt().set(inclusionMode).save(stream);

        return this;
    }

    @Override
    public CNodeEntityFilterInCube load(InputStream stream)
    {
        super.load(stream);

        inclusionMode = new CInt().load(stream).value;

        return this;
    }


    @SideOnly(Side.CLIENT)
    public static class EntityFilterInCubeNodeGUI extends GUIScreen
    {
        protected EntityFilterInCubeNodeGUI(CNodeEntityFilterInCube node)
        {
            show(node);
        }

        @Override
        public String title()
        {
            return "Entity Filter: 'In Cube' Node";
        }

        protected void show(CNodeEntityFilterInCube node)
        {
            show();


            //Background
            root.add(new GUIDarkenedBackground(this));


            //Header
            GUINavbar navbar = new GUINavbar(this);
            root.add(navbar);


            //Data
            GUIText inclusionModeLabel = new GUIText(this, "Inclusion Mode: ", getIdleColor(Color.WHITE), getHoverColor(Color.WHITE), Color.WHITE);
            GUIText inclusionMode = new GUIText(this, EntityFilters.INLUSION_MODES.get(node.inclusionMode), getIdleColor(Color.WHITE), getHoverColor(Color.WHITE), Color.WHITE);

            inclusionModeLabel.linkMouseActivity(inclusionMode);
            inclusionMode.linkMouseActivity(inclusionModeLabel);

            root.addAll(
                    inclusionModeLabel.addClickActions(inclusionMode::click),
                    inclusionMode.addClickActions(() ->
                    {
                        TextSelectionGUI gui = new TextSelectionGUI(inclusionMode, "Select Inclusion Mode", EntityFilters.INLUSION_MODES.values().toArray(new String[0]));
                        gui.addOnClosedActions(() ->
                        {
                            String result = inclusionMode.getText();
                            for (Map.Entry<Integer, String> entry : EntityFilters.INLUSION_MODES.entrySet())
                            {
                                if (entry.getValue().equals(result))
                                {
                                    node.inclusionMode = entry.getKey();
                                    break;
                                }
                            }
                        });
                    })
            );
        }
    }
}
