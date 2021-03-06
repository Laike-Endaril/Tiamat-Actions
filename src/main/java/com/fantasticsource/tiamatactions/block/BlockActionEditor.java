package com.fantasticsource.tiamatactions.block;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tiamatactions.Network;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class BlockActionEditor extends Block
{
    public BlockActionEditor()
    {
        super(Material.ROCK);
        setSoundType(SoundType.STONE);

        setBlockUnbreakable();
        setResistance(Float.MAX_VALUE);

        setCreativeTab(BlocksAndItems.creativeTab);

        setUnlocalizedName(MODID + ":actioneditor");
        setRegistryName("actioneditor");
    }


    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote) return true;

        if (MCTools.isOP((EntityPlayerMP) player)) Network.WRAPPER.sendTo(new Network.OpenMainActionEditorPacket(), (EntityPlayerMP) player);
        return true;
    }
}
