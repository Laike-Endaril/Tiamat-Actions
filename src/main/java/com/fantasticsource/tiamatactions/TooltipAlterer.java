package com.fantasticsource.tiamatactions;

import com.fantasticsource.mctools.ClientTickTimer;
import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.mctools.items.ItemMatcher;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@SideOnly(Side.CLIENT)
public class TooltipAlterer
{
    protected static List<String> lastTooltip = new ArrayList<>();
    protected static ItemStack lastTooltipStack = ItemStack.EMPTY;
    protected static long lastTooltipTick = 0;

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void tooltip(ItemTooltipEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();
        CAction action = CAction.ALL_ACTIONS.get(TiamatActionsConfig.clientSettings.tooltipAction);
        if (player == null || action == null || action.tickNodes.size() > 0) return;


        ItemStack stack = event.getItemStack();
        List<String> tooltip = event.getToolTip();
        long tick = ClientTickTimer.currentTick();
        if ((tick == lastTooltipTick || tick % 20 != 0) && ItemMatcher.stacksMatch(event.getItemStack(), lastTooltipStack))
        {
            tooltip.clear();
            tooltip.addAll(lastTooltip);
            return;
        }


        LinkedHashMap<String, Object> actionVars = new LinkedHashMap<>();
        actionVars.put("itemstack", stack);
        actionVars.put("tooltip", tooltip);
        action.queue(player, null, null, null, actionVars);

        lastTooltip.clear();
        lastTooltip.addAll(tooltip);
        lastTooltipStack = MCTools.cloneItemStack(stack);
        lastTooltipTick = tick;
    }
}
