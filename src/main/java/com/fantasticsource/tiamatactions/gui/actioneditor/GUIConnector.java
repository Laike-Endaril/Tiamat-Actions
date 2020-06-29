package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUILine;
import com.fantasticsource.mctools.gui.element.text.GUIFadingText;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.Tools;
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
        if (!halfPart && isMouseWithin())
        {
            EventEditorGUI gui = (EventEditorGUI) screen;

            switch (keyCode)
            {
                case Keyboard.KEY_DELETE:
                    to.removeInput(gui.action, from);
                    break;

                case Keyboard.KEY_1:
                case Keyboard.KEY_NUMPAD1:
                    reorder(0);
                    break;

                case Keyboard.KEY_2:
                case Keyboard.KEY_NUMPAD2:
                    reorder(1);
                    break;

                case Keyboard.KEY_3:
                case Keyboard.KEY_NUMPAD3:
                    reorder(2);
                    break;

                case Keyboard.KEY_4:
                case Keyboard.KEY_NUMPAD4:
                    reorder(3);
                    break;

                case Keyboard.KEY_5:
                case Keyboard.KEY_NUMPAD5:
                    reorder(4);
                    break;

                case Keyboard.KEY_6:
                case Keyboard.KEY_NUMPAD6:
                    reorder(5);
                    break;

                case Keyboard.KEY_7:
                case Keyboard.KEY_NUMPAD7:
                    reorder(6);
                    break;

                case Keyboard.KEY_8:
                case Keyboard.KEY_NUMPAD8:
                    reorder(7);
                    break;

                case Keyboard.KEY_9:
                case Keyboard.KEY_NUMPAD9:
                    reorder(8);
                    break;

                case Keyboard.KEY_0:
                case Keyboard.KEY_NUMPAD0:
                    reorder(9);
                    break;
            }

            gui.refreshNodeConnections();
        }

        super.keyTyped(typedChar, keyCode);
    }

    protected void reorder(int newInputIndex)
    {
        if (newInputIndex >= to.inputNodePositions.size())
        {
            parent.parent.add(new GUIFadingText(screen, parent.x + 5d / screen.pxWidth, parent.y + 5d / screen.pxHeight, "Cannot set input number; given input number is higher than current number of inputs", 150, 300, Color.RED));
            return;
        }

        long pos = Tools.getLong(from.y, from.x);
        to.inputNodePositions.remove(pos);
        to.inputNodePositions.add(newInputIndex, pos);
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
