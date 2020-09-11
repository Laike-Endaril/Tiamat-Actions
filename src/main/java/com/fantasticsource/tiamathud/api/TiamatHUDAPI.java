package com.fantasticsource.tiamathud.api;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;

public class TiamatHUDAPI
{
    private static ITiamatHUDNatives tiamatHUDAPIMethods = null;

    static
    {
        try
        {
            for (Field field : Class.forName("com.fantasticsource.tiamathud.apinatives.TiamatHUDNatives").getDeclaredFields())
            {
                if (field.getName().equals("NATIVES"))
                {
                    tiamatHUDAPIMethods = (ITiamatHUDNatives) field.get(null);
                }
            }
        }
        catch (ClassNotFoundException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }


    public static void sendCustomHUDData(EntityPlayerMP player, String key, double value)
    {
        if (tiamatHUDAPIMethods != null) tiamatHUDAPIMethods.sendCustomHUDData(player, key, value);
    }

    public static void sendCustomHUDData(EntityPlayerMP player, String key, String value)
    {
        if (tiamatHUDAPIMethods != null) tiamatHUDAPIMethods.sendCustomHUDData(player, key, value);
    }


    @SideOnly(Side.CLIENT)
    public static String getCustomHUDData(String key)
    {
        if (tiamatHUDAPIMethods != null) return tiamatHUDAPIMethods.getCustomHUDData(key);
        return null;
    }
}
