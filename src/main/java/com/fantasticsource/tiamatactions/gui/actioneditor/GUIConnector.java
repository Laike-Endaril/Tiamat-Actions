package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUILine;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.datastructures.Color;
import org.lwjgl.input.Keyboard;

public class GUIConnector extends GUILine
{
    public static final Color[]
            GREEN = new Color[]{GUIScreen.getIdleColor(Color.GREEN), GUIScreen.getHoverColor(Color.GREEN), Color.GREEN},
            RED = new Color[]{GUIScreen.getIdleColor(Color.RED), GUIScreen.getHoverColor(Color.RED), Color.RED};

    protected boolean halfPart;
    protected CNode from, to;

    public GUIConnector(GUIScreen screen, GUINodeView view, CNode from, CNode to, boolean halfPart)
    {
        super(screen, (double) from.x / view.absolutePxWidth(), (double) from.y / view.absolutePxHeight(), halfPart ? (from.x + to.x) * 0.5 / view.absolutePxWidth() : (double) to.x / view.absolutePxWidth(), halfPart ? (from.y + to.y) * 0.5 / view.absolutePxHeight() : (double) to.y / view.absolutePxHeight(), GREEN[0], GREEN[1], GREEN[2], halfPart ? 3 : 1);

        this.halfPart = halfPart;
        this.from = from;
        this.to = to;
    }


    @Override
    public void keyTyped(char typedChar, int keyCode)
    {
        if (!halfPart && keyCode == Keyboard.KEY_DELETE && isMouseWithin())
        {
            EventEditorGUI gui = (EventEditorGUI) screen;
            to.removeInput(gui.action, from);
            gui.refreshNodeConnections();
        }

        super.keyTyped(typedChar, keyCode);
    }


    @Override
    public boolean isWithin(double x, double y)
    {
        if (parent == null) return false;


        for (GUIElement element : parent.children.toArray(new GUIElement[0]))
        {
            if (element instanceof GUINode && element.isWithin(x, y)) return false;
        }

        return super.isWithin(x, y);
    }
}
