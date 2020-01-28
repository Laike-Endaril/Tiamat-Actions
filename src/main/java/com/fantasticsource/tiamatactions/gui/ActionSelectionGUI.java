package com.fantasticsource.tiamatactions.gui;

import com.fantasticsource.mctools.gui.screen.TextSelectionGUI;

public class ActionSelectionGUI extends TextSelectionGUI
{
    public static GUIAction lastClicked = null;

    public ActionSelectionGUI(String... options)
    {
        super(lastClicked, "Select Action...", options);
    }
}
