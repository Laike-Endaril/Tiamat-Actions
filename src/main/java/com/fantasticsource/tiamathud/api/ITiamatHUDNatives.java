package com.fantasticsource.tiamathud.api;

import net.minecraft.entity.player.EntityPlayerMP;

public interface ITiamatHUDNatives
{
    void sendCustomHUDData(EntityPlayerMP player, String key, double value);
}
