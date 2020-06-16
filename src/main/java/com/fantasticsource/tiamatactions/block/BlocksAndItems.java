package com.fantasticsource.tiamatactions.block;

import com.fantasticsource.tiamatactions.item.ItemTiamatActionsIcon;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class BlocksAndItems
{
    @GameRegistry.ObjectHolder(MODID + ":actioneditor")
    public static BlockActionEditor blockActionEditor;
    @GameRegistry.ObjectHolder(MODID + ":actioneditor")
    public static ItemActionEditor itemActionEditor;

    @GameRegistry.ObjectHolder(MODID + ":icon")
    public static ItemTiamatActionsIcon icon;


    public static CreativeTabs creativeTab = new CreativeTabs(MODID)
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(icon);
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> itemStacks)
        {
            super.displayAllRelevantItems(itemStacks);
        }
    };

    @SubscribeEvent
    public static void blockRegistry(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();

        registry.register(new BlockActionEditor());
    }

    @SubscribeEvent
    public static void itemRegistry(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();

        registry.register(new ItemActionEditor());

        registry.register(new ItemTiamatActionsIcon());
    }

    @SubscribeEvent
    public static void modelRegistry(ModelRegistryEvent event)
    {
        ModelLoader.setCustomModelResourceLocation(itemActionEditor, 0, new ModelResourceLocation(MODID + ":actioneditor", "inventory"));

        ModelLoader.setCustomModelResourceLocation(icon, 0, new ModelResourceLocation(MODID + ":icon", "inventory"));
    }
}
