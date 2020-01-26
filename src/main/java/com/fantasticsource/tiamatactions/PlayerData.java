package com.fantasticsource.tiamatactions;

import com.fantasticsource.tiamatactions.action.Action;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.LinkedHashMap;

public class PlayerData
{
    public static final LinkedHashMap<EntityPlayerMP, PlayerData> PLAYER_DATA = new LinkedHashMap<>();


    public Action currentAction = null;
    public Action queuedAction = null;


    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        PLAYER_DATA.put((EntityPlayerMP) event.player, new PlayerData());
    }

    @SubscribeEvent
    public static void playerLogout(PlayerEvent.PlayerLoggedOutEvent event)
    {
        PLAYER_DATA.remove(event.player);
    }
}
