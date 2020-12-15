package com.fantasticsource.tiamatactions.config.client;

import net.minecraftforge.common.config.Config;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class ClientConfig
{
    @Config.Name("GUI Settings")
    @Config.LangKey(MODID + ".config.guiSettings")
    public GUIConfig guiSettings = new GUIConfig();

    @Config.Name("Tooltip Action")
    @Config.LangKey(MODID + ".config.tooltipAction")
    @Config.Comment(
            {
                    "An action used to alter tooltips.  This action must not have tick nodes.",
                    "The following action vars will exist before the action executes:",
                    "itemstack = the itemstack related to the tooltip (if any)",
                    "tooltip = the original tooltip, as a list of strings (alter these to change the tooltip)"
            })
    public String tooltipAction = "";
}
