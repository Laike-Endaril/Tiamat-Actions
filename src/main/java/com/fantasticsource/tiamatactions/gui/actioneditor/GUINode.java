package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.datastructures.Color;

public class GUINode extends GUIText
{
    protected CNode node;

    public GUINode(GUIScreen screen, double x, double y, CNode node)
    {
        super(screen, x, y, node.getDescription(), GUIScreen.getIdleColor(Color.WHITE), GUIScreen.getHoverColor(Color.WHITE), Color.WHITE, 0.5);
        this.node = node;
    }


    @Override
    public void click()
    {
        node.showNodeEditGUI().addOnClosedActions(() -> setText(node.getDescription()));

        super.click();
    }
}
