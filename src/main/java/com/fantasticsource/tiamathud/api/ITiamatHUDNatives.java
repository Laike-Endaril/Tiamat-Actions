package com.fantasticsource.tiamathud.api;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ITiamatHUDNatives
{
    void sendCustomHUDData(EntityPlayerMP player, String key, double value);

    void sendCustomHUDData(EntityPlayerMP player, String key, String value);


    @SideOnly(Side.CLIENT)
    String getCustomHUDData(String key);
}
