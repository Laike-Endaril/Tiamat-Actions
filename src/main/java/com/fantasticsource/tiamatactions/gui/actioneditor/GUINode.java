package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.textured.GUIImage;
import com.fantasticsource.tiamatactions.node.CNode;
import net.minecraft.util.text.TextFormatting;

public class GUINode extends GUIImage
{
    public static final int SIZE = 32;

    protected CNode node;

    public GUINode(GUIScreen screen, double x, double y, CNode node)
    {
        super(screen, x, y, SIZE, SIZE, node.getTexture());
        this.node = node;
        updateTooltip();
    }


    @Override
    protected void tick()
    {
        updateTooltip();
    }


    protected void updateTooltip()
    {
        String error = node.error();
        setTooltip(error == null ? node.getDescription() : node.getDescription() + " (" + error + ")");
    }


    @Override
    public void click()
    {
        node.showNodeEditGUI().addOnClosedActions(this::updateTooltip);

        super.click();
    }
}
