package com.fantasticsource.tiamatactions.config.client;

import net.minecraftforge.common.config.Config;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class GUIConfig
{
    @Config.Name("Create / Edit Node (Mouse Button)")
    @Config.LangKey(MODID + ".config.createEditNodeButton")
    @Config.Comment(
            {
                    "Which mouse button creates, edits, and moves nodes in a node view (click for add/edit, drag for move)",
            })
    @Config.RangeInt(min = 0)
    public int createEditNodeButton = 0;

    @Config.Name("Create Node Connection (Mouse Button)")
    @Config.LangKey(MODID + ".config.createNodeConnectionButton")
    @Config.Comment(
            {
                    "Which mouse button creates connections between nodes in a node view (drag OR click one node, then next)",
            })
    @Config.RangeInt(min = 0)
    public int createNodeConnectionButton = 1;
}
