package com.fantasticsource.tiamatactions.gui;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.tiamatactions.Network;
import com.fantasticsource.tools.datastructures.Color;

public class GUIAction extends GUIText
{
    public GUIAction(GUIScreen screen)
    {
        this(screen, "None");
    }

    public GUIAction(GUIScreen screen, String text)
    {
        super(screen, text, GUIScreen.getIdleColor(Color.WHITE), GUIScreen.getHoverColor(Color.WHITE), Color.WHITE);
    }

    @Override
    public void click()
    {
        ActionSelectionGUI.lastClicked = this;
        Network.WRAPPER.sendToServer(new Network.RequestOpenActionSelectorPacket());

        super.click();
    }
}
