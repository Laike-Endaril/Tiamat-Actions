package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.ImprovedRayTracing;
import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUIDarkenedBackground;
import com.fantasticsource.mctools.gui.element.text.GUILabeledBoolean;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.data.Ray;
import com.fantasticsource.tools.component.CBoolean;
import com.fantasticsource.tools.datastructures.Pair;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeRayCollisionVector extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/ray_collision_vector.png");
    protected static final Pair<String, Class> OPTIONAL_INPUTS = new Pair<>("ignored", Entity.class);
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("world", World.class);
        REQUIRED_INPUTS.put("ray", Ray.class);
        REQUIRED_INPUTS.put("range", Object.class);
        REQUIRED_INPUTS.put("collideOnAllSolids", Boolean.class);
    }

    protected boolean collideWithBlocks = true, collideWithEntities = true, returnNullOnMiss = true;

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeRayCollisionVector()
    {
        super();
    }

    public CNodeRayCollisionVector(String actionName, String event, int x, int y)
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
        return "Get the collision point of the given ray";
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
        return Vec3d.class;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        World world = (World) inputs[0];
        Ray ray = (Ray) inputs[1];
        double range = Double.parseDouble("" + inputs[2]);
        Vec3d start = ray.origin, end = ray.origin.add(ray.direction.scale(range));
        boolean collideOnAllSolids = (boolean) inputs[3];
        ArrayList<Entity> ignored = new ArrayList<>();
        for (int i = 4; i < inputs.length; i++) ignored.add((Entity) inputs[i]);
        boolean collided = false;

        if (collideWithEntities)
        {
            for (Entity entity : world.loadedEntityList)
            {
                if (ignored.contains(entity)) continue;

                RayTraceResult rayTraceResult = ImprovedRayTracing.rayTraceEntity(entity, start, end);
                if (rayTraceResult != null)
                {
                    end = rayTraceResult.hitVec;
                    collided = true;
                }
            }
        }

        if (collideWithBlocks)
        {
            RayTraceResult result = ImprovedRayTracing.rayTraceBlocks(world, start, end, collideOnAllSolids);
            if (result.hitVec != null)
            {
                end = result.hitVec;
                collided = true;
            }
        }

        return collided || !returnNullOnMiss ? end : null;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen showNodeEditGUI()
    {
        return new RayCollisionVectorNodeGUI(this);
    }


    @Override
    public CNodeRayCollisionVector write(ByteBuf buf)
    {
        super.write(buf);

        buf.writeBoolean(collideWithBlocks);
        buf.writeBoolean(collideWithEntities);
        buf.writeBoolean(returnNullOnMiss);

        return this;
    }

    @Override
    public CNodeRayCollisionVector read(ByteBuf buf)
    {
        super.read(buf);

        collideWithBlocks = buf.readBoolean();
        collideWithEntities = buf.readBoolean();
        returnNullOnMiss = buf.readBoolean();

        return this;
    }

    @Override
    public CNodeRayCollisionVector save(OutputStream stream)
    {
        super.save(stream);

        new CBoolean().set(collideWithBlocks).save(stream);
        new CBoolean().set(collideWithEntities).save(stream);
        new CBoolean().set(returnNullOnMiss).save(stream);

        return this;
    }

    @Override
    public CNodeRayCollisionVector load(InputStream stream)
    {
        super.load(stream);

        collideWithBlocks = new CBoolean().load(stream).value;
        collideWithEntities = new CBoolean().load(stream).value;
        returnNullOnMiss = new CBoolean().load(stream).value;

        return this;
    }


    @SideOnly(Side.CLIENT)
    public static class RayCollisionVectorNodeGUI extends GUIScreen
    {
        protected RayCollisionVectorNodeGUI(CNodeRayCollisionVector node)
        {
            show(node);
        }

        @Override
        public String title()
        {
            return "Ray Collision Vector Node";
        }

        protected void show(CNodeRayCollisionVector node)
        {
            show();


            //Background
            root.add(new GUIDarkenedBackground(this));


            //Header
            GUINavbar navbar = new GUINavbar(this);
            root.add(navbar);


            //Data
            GUILabeledBoolean collideWithBlocks = new GUILabeledBoolean(this, "Collide with Blocks: ", node.collideWithBlocks);
            GUILabeledBoolean collideWithEntities = new GUILabeledBoolean(this, "Collide with Entities: ", node.collideWithEntities);
            GUILabeledBoolean returnNullOnMiss = new GUILabeledBoolean(this, "Return Null on Miss: ", node.returnNullOnMiss);

            root.addAll(
                    collideWithBlocks.addClickActions(() -> node.collideWithBlocks = collideWithBlocks.getValue()),
                    new GUIElement(this, 1, 0),
                    collideWithEntities.addClickActions(() -> node.collideWithEntities = collideWithEntities.getValue()),
                    new GUIElement(this, 1, 0),
                    returnNullOnMiss.addClickActions(() -> node.returnNullOnMiss = returnNullOnMiss.getValue())
            );
        }
    }
}
