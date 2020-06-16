package com.fantasticsource.tiamatactions.block;

import net.minecraft.item.ItemBlock;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class ItemActionEditor extends ItemBlock
{
    public ItemActionEditor()
    {
        super(BlocksAndItems.blockActionEditor);

        setUnlocalizedName(MODID + ":actioneditor");
        setRegistryName("actioneditor");
    }
}
