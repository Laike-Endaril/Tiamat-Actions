package com.fantasticsource.tiamatactions.node;

import com.fantasticsource.mctools.GlobalInventory;
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
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class CNodeGetItemstack extends CNode
{
    protected static final Color[]
            WHITE = new Color[]{GUIScreen.getIdleColor(Color.WHITE), GUIScreen.getHoverColor(Color.WHITE), Color.WHITE},
            PURPLE = new Color[]{GUIScreen.getIdleColor(Color.PURPLE), GUIScreen.getHoverColor(Color.PURPLE), Color.PURPLE};

    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/get_itemstack.png");
    protected static final Pair<String, Class> OPTIONAL_INPUTS = new Pair<>("slotNumber", String.class);
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("entity", Entity.class);
    }

    public static final int
            TYPE_MAINHAND = 0,
            TYPE_OFFHAND = 1,
            TYPE_HEAD = 2,
            TYPE_CHEST = 3,
            TYPE_LEG = 4,
            TYPE_FOOT = 5,
            TYPE_TIAMAT_SHEATHED_MAINHAND_1 = 6,
            TYPE_TIAMAT_SHEATHED_OFFHAND_1 = 7,
            TYPE_TIAMAT_SHEATHED_MAINHAND_2 = 8,
            TYPE_TIAMAT_SHEATHED_OFFHAND_2 = 9,
            TYPE_TIAMAT_SHOULDER = 10,
            TYPE_TIAMAT_CAPE = 11,
            TYPE_TIAMAT_QUICKSLOT = 12,
            TYPE_TIAMAT_BACKPACK = 13,
            TYPE_TIAMAT_PET = 14,
            TYPE_TIAMAT_DECK = 15,
            TYPE_TIAMAT_CLASS = 16,
            TYPE_TIAMAT_OFFENSIVE_SKILL = 17,
            TYPE_TIAMAT_UTILITY_SKILL = 18,
            TYPE_TIAMAT_ULTIMATE_SKILL = 19,
            TYPE_TIAMAT_PASSIVE_SKILL = 20,
            TYPE_TIAMAT_GATHERING_PROFESSION = 21,
            TYPE_TIAMAT_CRAFTING_PROFESSION = 22,
            TYPE_TIAMAT_RECIPE = 23;

    protected int type = TYPE_MAINHAND;

    /**
     * ONLY MEANT FOR USE WITH COMPONENT FUNCTIONS!
     */
    public CNodeGetItemstack()
    {
        super();
    }

    public CNodeGetItemstack(String actionName, String event, int x, int y)
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
            case TYPE_MAINHAND:
                return "Get an entity's wielded mainhand item";

            case TYPE_OFFHAND:
                return "Get an entity's wielded offhand item";

            case TYPE_HEAD:
                return "Get an entity's head item";

            case TYPE_CHEST:
                return "Get an entity's chest item";

            case TYPE_LEG:
                return "Get an entity's leg item";

            case TYPE_FOOT:
                return "Get an entity's foot item";

            case TYPE_TIAMAT_SHEATHED_MAINHAND_1:
                return "Get an entity's 1st sheathed mainhand item";

            case TYPE_TIAMAT_SHEATHED_OFFHAND_1:
                return "Get an entity's 1st sheathed offhand item";

            case TYPE_TIAMAT_SHEATHED_MAINHAND_2:
                return "Get an entity's 2nd sheathed mainhand item";

            case TYPE_TIAMAT_SHEATHED_OFFHAND_2:
                return "Get an entity's 2nd sheathed offhand item";

            case TYPE_TIAMAT_SHOULDER:
                return "Get an entity's shoulder item";

            case TYPE_TIAMAT_CAPE:
                return "Get an entity's cape item";

            case TYPE_TIAMAT_QUICKSLOT:
                return "Get an entity's quickslot item";

            case TYPE_TIAMAT_BACKPACK:
                return "Get an entity's backpack item";

            case TYPE_TIAMAT_PET:
                return "Get an entity's pet item";

            case TYPE_TIAMAT_DECK:
                return "Get an entity's deck item";

            case TYPE_TIAMAT_CLASS:
                return "Get an entity's class item";

            case TYPE_TIAMAT_OFFENSIVE_SKILL:
                return "Get an entity's offensive skill item";

            case TYPE_TIAMAT_UTILITY_SKILL:
                return "Get an entity's utility skill item";

            case TYPE_TIAMAT_ULTIMATE_SKILL:
                return "Get an entity's ultimate skill item";

            case TYPE_TIAMAT_PASSIVE_SKILL:
                return "Get an entity's passive skill item";

            case TYPE_TIAMAT_GATHERING_PROFESSION:
                return "Get an entity's gathering profession item";

            case TYPE_TIAMAT_CRAFTING_PROFESSION:
                return "Get an entity's crafting profession item";

            case TYPE_TIAMAT_RECIPE:
                return "Get an entity's equipped recipe item";

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
        int index = inputs.length > 1 ? Integer.parseInt("" + inputs[1]) - 1 : 0;
        ArrayList<ItemStack> items;

        switch (type)
        {
            case TYPE_MAINHAND:
                return GlobalInventory.getVanillaMainhandItem((Entity) inputs[0]);

            case TYPE_OFFHAND:
                items = GlobalInventory.getVanillaOffhandItems((Entity) inputs[0]);
                break;

            case TYPE_HEAD:
                return GlobalInventory.getVanillaHeadItem((Entity) inputs[0]);

            case TYPE_CHEST:
                return GlobalInventory.getVanillaChestItem((Entity) inputs[0]);

            case TYPE_LEG:
                return GlobalInventory.getVanillaLegItem((Entity) inputs[0]);

            case TYPE_FOOT:
                return GlobalInventory.getVanillaFootItem((Entity) inputs[0]);

            case TYPE_TIAMAT_SHEATHED_MAINHAND_1:
                //TODO
                return null;

            case TYPE_TIAMAT_SHEATHED_OFFHAND_1:
                //TODO
                return null;

            case TYPE_TIAMAT_SHEATHED_MAINHAND_2:
                //TODO
                return null;

            case TYPE_TIAMAT_SHEATHED_OFFHAND_2:
                //TODO
                return null;

            case TYPE_TIAMAT_SHOULDER:
                return GlobalInventory.getTiamatShoulderItem((Entity) inputs[0]);

            case TYPE_TIAMAT_CAPE:
                return GlobalInventory.getTiamatCapeItem((Entity) inputs[0]);

            case TYPE_TIAMAT_QUICKSLOT:
                items = GlobalInventory.getTiamatQuickslots((Entity) inputs[0]);
                break;

            case TYPE_TIAMAT_BACKPACK:
                return GlobalInventory.getTiamatBackpack((Entity) inputs[0]);

            case TYPE_TIAMAT_PET:
                return GlobalInventory.getTiamatPet((Entity) inputs[0]);

            case TYPE_TIAMAT_DECK:
                return GlobalInventory.getTiamatDeck((Entity) inputs[0]);

            case TYPE_TIAMAT_CLASS:
                items = GlobalInventory.getTiamatClasses((Entity) inputs[0]);
                break;

            case TYPE_TIAMAT_OFFENSIVE_SKILL:
                items = GlobalInventory.getTiamatOffensiveSkills((Entity) inputs[0]);
                break;

            case TYPE_TIAMAT_UTILITY_SKILL:
                items = GlobalInventory.getTiamatUtilitySkills((Entity) inputs[0]);
                break;

            case TYPE_TIAMAT_ULTIMATE_SKILL:
                return GlobalInventory.getTiamatUltimateSkill((Entity) inputs[0]);

            case TYPE_TIAMAT_PASSIVE_SKILL:
                items = GlobalInventory.getTiamatPassiveSkills((Entity) inputs[0]);
                break;

            case TYPE_TIAMAT_GATHERING_PROFESSION:
                items = GlobalInventory.getTiamatGatheringProfessions((Entity) inputs[0]);
                break;

            case TYPE_TIAMAT_CRAFTING_PROFESSION:
                items = GlobalInventory.getTiamatCraftingProfessions((Entity) inputs[0]);
                break;

            case TYPE_TIAMAT_RECIPE:
                items = GlobalInventory.getTiamatRecipes((Entity) inputs[0]);
                break;

            default:
                throw new IllegalStateException("Invalid item slot type: " + type);
        }

        return items.size() > index ? items.get(index) : null;
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen showNodeEditGUI()
    {
        return new GetItemstackNodeGUI(this);
    }


    @Override
    public CNodeGetItemstack write(ByteBuf buf)
    {
        super.write(buf);

        buf.writeInt(type);

        return this;
    }

    @Override
    public CNodeGetItemstack read(ByteBuf buf)
    {
        super.read(buf);

        type = buf.readInt();

        return this;
    }

    @Override
    public CNodeGetItemstack save(OutputStream stream)
    {
        super.save(stream);

        new CInt().set(type).save(stream);

        return this;
    }

    @Override
    public CNodeGetItemstack load(InputStream stream)
    {
        super.load(stream);

        type = new CInt().load(stream).value;

        return this;
    }


    public static class GetItemstackNodeGUI extends GUIScreen
    {
        protected GetItemstackNodeGUI(CNodeGetItemstack node)
        {
            show(node);
        }

        @Override
        public String title()
        {
            return "Get Itemstack Node";
        }

        protected void show(CNodeGetItemstack node)
        {
            show();


            //Background
            root.add(new GUIDarkenedBackground(this));


            //Header
            GUINavbar navbar = new GUINavbar(this);
            root.add(navbar);


            //Data
            Color[] color = node.type == TYPE_MAINHAND ? PURPLE : WHITE;
            root.add(new GUIText(this, "Mainhand", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_MAINHAND;
                close();
            }));

            color = node.type == TYPE_OFFHAND ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Offhand", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_OFFHAND;
                close();
            }));

            color = node.type == TYPE_HEAD ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Head", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_HEAD;
                close();
            }));

            color = node.type == TYPE_CHEST ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Chest", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_CHEST;
                close();
            }));

            color = node.type == TYPE_LEG ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Leg", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_LEG;
                close();
            }));

            color = node.type == TYPE_FOOT ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Foot", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_FOOT;
                close();
            }));

            color = node.type == TYPE_TIAMAT_SHEATHED_MAINHAND_1 ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Sheathed Mainhand 1", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_SHEATHED_MAINHAND_1;
                close();
            }));

            color = node.type == TYPE_TIAMAT_SHEATHED_OFFHAND_1 ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Sheathed Offhand 1", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_SHEATHED_OFFHAND_1;
                close();
            }));

            color = node.type == TYPE_TIAMAT_SHEATHED_MAINHAND_2 ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Sheathed Mainhand 2", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_SHEATHED_MAINHAND_2;
                close();
            }));

            color = node.type == TYPE_TIAMAT_SHEATHED_OFFHAND_2 ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Sheathed Offhand 2", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_SHEATHED_OFFHAND_2;
                close();
            }));

            color = node.type == TYPE_TIAMAT_SHOULDER ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Tiamat Shoulder", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_SHOULDER;
                close();
            }));

            color = node.type == TYPE_TIAMAT_CAPE ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Tiamat Cape", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_CAPE;
                close();
            }));

            color = node.type == TYPE_TIAMAT_QUICKSLOT ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Tiamat Quickslot", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_QUICKSLOT;
                close();
            }));

            color = node.type == TYPE_TIAMAT_BACKPACK ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Tiamat Backpack", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_BACKPACK;
                close();
            }));

            color = node.type == TYPE_TIAMAT_PET ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Tiamat Pet", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_PET;
                close();
            }));

            color = node.type == TYPE_TIAMAT_DECK ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Tiamat Deck", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_DECK;
                close();
            }));

            color = node.type == TYPE_TIAMAT_CLASS ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Tiamat Class", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_CLASS;
                close();
            }));

            color = node.type == TYPE_TIAMAT_OFFENSIVE_SKILL ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Tiamat Offensive Skill", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_OFFENSIVE_SKILL;
                close();
            }));

            color = node.type == TYPE_TIAMAT_UTILITY_SKILL ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Tiamat Utility Skill", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_UTILITY_SKILL;
                close();
            }));

            color = node.type == TYPE_TIAMAT_ULTIMATE_SKILL ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Tiamat Ultimate Skill", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_ULTIMATE_SKILL;
                close();
            }));

            color = node.type == TYPE_TIAMAT_PASSIVE_SKILL ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Tiamat Passive Skill", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_PASSIVE_SKILL;
                close();
            }));

            color = node.type == TYPE_TIAMAT_GATHERING_PROFESSION ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Tiamat Gathering Profession", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_GATHERING_PROFESSION;
                close();
            }));

            color = node.type == TYPE_TIAMAT_CRAFTING_PROFESSION ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Tiamat Crafting Profession", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_CRAFTING_PROFESSION;
                close();
            }));

            color = node.type == TYPE_TIAMAT_RECIPE ? PURPLE : WHITE;
            root.add(new GUIElement(this, 1, 0));
            root.add(new GUIText(this, "Tiamat Equipped Recipe", color[0], color[1], color[2]).addClickActions(() ->
            {
                node.type = TYPE_TIAMAT_RECIPE;
                close();
            }));
        }
    }
}
