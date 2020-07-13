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

public class CNodeSetItemstack extends CNode
{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "image/node/set_itemstack.png");
    protected static final Pair<String, Class> OPTIONAL_INPUTS = new Pair<>("slotNumber", Object.class);
    protected static final LinkedHashMap<String, Class> REQUIRED_INPUTS = new LinkedHashMap<>();

    static
    {
        REQUIRED_INPUTS.put("entity", Entity.class);
        REQUIRED_INPUTS.put("itemstack", ItemStack.class);
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
    public CNodeSetItemstack()
    {
        super();
    }

    public CNodeSetItemstack(String actionName, String event, int x, int y)
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
                return "Set an entity's wielded mainhand item";

            case TYPE_OFFHAND:
                return "Set an entity's wielded offhand item";

            case TYPE_HEAD:
                return "Set an entity's head item";

            case TYPE_CHEST:
                return "Set an entity's chest item";

            case TYPE_LEG:
                return "Set an entity's leg item";

            case TYPE_FOOT:
                return "Set an entity's foot item";

            case TYPE_TIAMAT_SHEATHED_MAINHAND_1:
                return "Set an entity's 1st sheathed mainhand item";

            case TYPE_TIAMAT_SHEATHED_OFFHAND_1:
                return "Set an entity's 1st sheathed offhand item";

            case TYPE_TIAMAT_SHEATHED_MAINHAND_2:
                return "Set an entity's 2nd sheathed mainhand item";

            case TYPE_TIAMAT_SHEATHED_OFFHAND_2:
                return "Set an entity's 2nd sheathed offhand item";

            case TYPE_TIAMAT_SHOULDER:
                return "Set an entity's shoulder item";

            case TYPE_TIAMAT_CAPE:
                return "Set an entity's cape item";

            case TYPE_TIAMAT_QUICKSLOT:
                return "Set an entity's quickslot item";

            case TYPE_TIAMAT_BACKPACK:
                return "Set an entity's backpack item";

            case TYPE_TIAMAT_PET:
                return "Set an entity's pet item";

            case TYPE_TIAMAT_DECK:
                return "Set an entity's deck item";

            case TYPE_TIAMAT_CLASS:
                return "Set an entity's class item";

            case TYPE_TIAMAT_OFFENSIVE_SKILL:
                return "Set an entity's offensive skill item";

            case TYPE_TIAMAT_UTILITY_SKILL:
                return "Set an entity's utility skill item";

            case TYPE_TIAMAT_ULTIMATE_SKILL:
                return "Set an entity's ultimate skill item";

            case TYPE_TIAMAT_PASSIVE_SKILL:
                return "Set an entity's passive skill item";

            case TYPE_TIAMAT_GATHERING_PROFESSION:
                return "Set an entity's gathering profession item";

            case TYPE_TIAMAT_CRAFTING_PROFESSION:
                return "Set an entity's crafting profession item";

            case TYPE_TIAMAT_RECIPE:
                return "Set an entity's equipped recipe item";

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
        return ItemStack.class;
    }


    @Override
    public Object execute(CAction mainAction, CAction subAction, Object... inputs)
    {
        int index = inputs.length > 2 ? Integer.parseInt("" + inputs[2]) - 1 : 0;

        switch (type)
        {
            case TYPE_MAINHAND:
                GlobalInventory.setVanillaMainhandItem((Entity) inputs[0], (ItemStack) inputs[1]);
                return null;

            case TYPE_OFFHAND:
                GlobalInventory.setVanillaOffhandItem((Entity) inputs[0], (ItemStack) inputs[1]);
                return null;

            case TYPE_HEAD:
                GlobalInventory.setVanillaHeadItem((Entity) inputs[0], (ItemStack) inputs[1]);
                return null;

            case TYPE_CHEST:
                GlobalInventory.setVanillaChestItem((Entity) inputs[0], (ItemStack) inputs[1]);
                return null;

            case TYPE_LEG:
                GlobalInventory.setVanillaLegItem((Entity) inputs[0], (ItemStack) inputs[1]);
                return null;

            case TYPE_FOOT:
                GlobalInventory.setVanillaFootItem((Entity) inputs[0], (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_SHEATHED_MAINHAND_1:
                GlobalInventory.setTiamatSheathedMainhand1((Entity) inputs[0], (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_SHEATHED_OFFHAND_1:
                GlobalInventory.setTiamatSheathedOffhand1((Entity) inputs[0], (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_SHEATHED_MAINHAND_2:
                GlobalInventory.setTiamatSheathedMainhand2((Entity) inputs[0], (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_SHEATHED_OFFHAND_2:
                GlobalInventory.setTiamatSheathedOffhand2((Entity) inputs[0], (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_SHOULDER:
                GlobalInventory.setTiamatShoulderItem((Entity) inputs[0], (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_CAPE:
                GlobalInventory.setTiamatCapeItem((Entity) inputs[0], (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_QUICKSLOT:
                GlobalInventory.setTiamatQuickslot((Entity) inputs[0], index, (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_BACKPACK:
                GlobalInventory.setTiamatBackpack((Entity) inputs[0], (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_PET:
                GlobalInventory.setTiamatPet((Entity) inputs[0], (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_DECK:
                GlobalInventory.setTiamatDeck((Entity) inputs[0], (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_CLASS:
                GlobalInventory.setTiamatClass((Entity) inputs[0], index, (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_OFFENSIVE_SKILL:
                GlobalInventory.setTiamatOffensiveSkill((Entity) inputs[0], index, (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_UTILITY_SKILL:
                GlobalInventory.setTiamatUtilitySkill((Entity) inputs[0], index, (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_ULTIMATE_SKILL:
                GlobalInventory.setTiamatUltimateSkill((Entity) inputs[0], (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_PASSIVE_SKILL:
                GlobalInventory.setTiamatPassiveSkill((Entity) inputs[0], index, (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_GATHERING_PROFESSION:
                GlobalInventory.setTiamatGatheringProfession((Entity) inputs[0], index, (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_CRAFTING_PROFESSION:
                GlobalInventory.setTiamatCraftingProfession((Entity) inputs[0], index, (ItemStack) inputs[1]);
                return null;

            case TYPE_TIAMAT_RECIPE:
                GlobalInventory.setTiamatRecipe((Entity) inputs[0], index, (ItemStack) inputs[1]);
                return null;

            default:
                throw new IllegalStateException("Invalid item slot type: " + type);
        }
    }


    @SideOnly(Side.CLIENT)
    @Override
    public GUIScreen showNodeEditGUI()
    {
        return new SetItemstackNodeGUI(this);
    }


    @Override
    public CNodeSetItemstack write(ByteBuf buf)
    {
        super.write(buf);

        buf.writeInt(type);

        return this;
    }

    @Override
    public CNodeSetItemstack read(ByteBuf buf)
    {
        super.read(buf);

        type = buf.readInt();

        return this;
    }

    @Override
    public CNodeSetItemstack save(OutputStream stream)
    {
        super.save(stream);

        new CInt().set(type).save(stream);

        return this;
    }

    @Override
    public CNodeSetItemstack load(InputStream stream)
    {
        super.load(stream);

        type = new CInt().load(stream).value;

        return this;
    }


    @SideOnly(Side.CLIENT)
    public static class SetItemstackNodeGUI extends GUIScreen
    {
        protected static final Color[]
                WHITE = new Color[]{GUIScreen.getIdleColor(Color.WHITE), GUIScreen.getHoverColor(Color.WHITE), Color.WHITE},
                PURPLE = new Color[]{GUIScreen.getIdleColor(Color.PURPLE), GUIScreen.getHoverColor(Color.PURPLE), Color.PURPLE};

        protected SetItemstackNodeGUI(CNodeSetItemstack node)
        {
            show(node);
        }

        @Override
        public String title()
        {
            return "Set Itemstack Node";
        }

        protected void show(CNodeSetItemstack node)
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
