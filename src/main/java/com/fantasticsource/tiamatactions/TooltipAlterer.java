package com.fantasticsource.tiamatactions;

import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedHashMap;

@SideOnly(Side.CLIENT)
public class TooltipAlterer
{
    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if (player == null || player instanceof EntityPlayerMP) return;


        CAction action = CAction.ALL_ACTIONS.get(TiamatActionsConfig.clientSettings.tooltipAction);
        if (action == null || action.tickNodes.size() > 0) return;

        LinkedHashMap<String, Object> actionVars = new LinkedHashMap<>();
        actionVars.put("itemstack", event.getItemStack());
        actionVars.put("tooltip", event.getToolTip());
        action.queue(player, null, null, null, actionVars);
    }
}
