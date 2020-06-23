package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.textured.GUIImage;
import com.fantasticsource.tiamatactions.node.CNode;

public class GUINode extends GUIImage
{
    public static final int SIZE = 32;

    protected CNode node;

    public GUINode(GUIScreen screen, double x, double y, CNode node)
    {
        super(screen, x, y, SIZE, SIZE, node.getTexture());
        setTooltip(node.getDescription());
        this.node = node;
    }


    @Override
    public void click()
    {
        node.showNodeEditGUI().addOnClosedActions(() -> setTooltip(node.getDescription()));

        super.click();
    }
}
