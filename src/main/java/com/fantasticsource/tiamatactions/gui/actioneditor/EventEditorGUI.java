package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUIGradient;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tiamatactions.node.CNodeTestCondition;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.datastructures.Color;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Map;

public class EventEditorGUI extends GUIScreen
{
    public static final int
            VIEW_MODE_INPUT_ORDER_PER_NODE = 0,
            VIEW_MODE_INPUT_ORDER_GLOBAL = 1;

    public static int viewMode = VIEW_MODE_INPUT_ORDER_GLOBAL;

    protected static final Color[] WHITES = new Color[]{getIdleColor(Color.WHITE), getHoverColor(Color.WHITE), Color.WHITE};

    protected CAction action;
    protected String event;
    protected GUINodeView view;

    public EventEditorGUI(CAction action, String event)
    {
        show(action, event);
    }

    protected void show(CAction action, String event)
    {
        show();
        drawStack = false;


        this.action = action;
        this.event = event;


        //Background
        root.add(new GUIGradient(this, 0, 0, 1, 1, Color.BLACK));


        //Header
        GUINavbar navbar = new GUINavbar(this);
        root.add(navbar);


        //Node view
        view = new GUINodeView(this, 1, 1 - navbar.height);
        root.add(view);
        double wConversion = 1d / view.absolutePxWidth(), hConversion = 1d / view.absolutePxHeight();
        for (CNode node : action.EVENT_NODES.get(event).values())
        {
            view.add(new GUINode(this, (node.x - GUINode.HALF_SIZE) * wConversion, (node.y - GUINode.HALF_SIZE) * hConversion, node));
        }
        refreshNodeConnections();


        //GUI Actions
        navbar.addRecalcActions(() -> view.height = 1 - navbar.height);
        recalc();
        view.focus(null);
    }


    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == Keyboard.KEY_1 || keyCode == Keyboard.KEY_NUMPAD1)
        {
            if (viewMode != 0 && !mouseIsWithinConnector())
            {
                viewMode = 0;
                refreshNodeConnections();
            }
        }
        else if (keyCode == Keyboard.KEY_2 || keyCode == Keyboard.KEY_NUMPAD2)
        {
            if (viewMode != 1 && !mouseIsWithinConnector())
            {
                viewMode = 1;
                refreshNodeConnections();
            }
        }

        super.keyTyped(typedChar, keyCode);
    }

    protected boolean mouseIsWithinConnector()
    {
        for (GUIElement element : view.children.toArray(new GUIElement[0]))
        {
            if (element instanceof GUIConnector && element.isMouseWithin()) return true;
        }
        return false;
    }


    public void refreshNodeConnections()
    {
        for (GUIElement element : view.children.toArray(new GUIElement[0]))
        {
            if (element instanceof GUIConnector) view.remove(element);
        }

        if (viewMode == VIEW_MODE_INPUT_ORDER_PER_NODE)
        {
            for (CNode node : action.EVENT_NODES.get(event).values())
            {
                for (long position : node.conditionNodePositions) refreshNodeConnection(node, position, 0);
                for (long position : node.inputNodePositions) refreshNodeConnection(node, position, 0);
            }
        }
        else
        {
            refreshNodeConnectionsGlobal(null, action.EVENT_ENDPOINT_NODES.get(event).toArray(new CNode[0]), 0);
        }
    }

    protected int refreshNodeConnectionsGlobal(CNode outputNode, CNode[] nodes, int i)
    {
        for (CNode node : nodes)
        {
            CNode[] conditionNodes = new CNode[node.conditionNodePositions.size()];
            int i2 = 0;
            for (long position : node.conditionNodePositions)
            {
                conditionNodes[i2++] = action.EVENT_NODES.get(node.eventName).get(position);
            }
            i = refreshNodeConnectionsGlobal(node, conditionNodes, i);

            CNode[] inputNodes = new CNode[node.inputNodePositions.size()];
            i2 = 0;
            for (long position : node.inputNodePositions)
            {
                inputNodes[i2++] = action.EVENT_NODES.get(node.eventName).get(position);
            }
            i = refreshNodeConnectionsGlobal(node, inputNodes, i);

            long position = Tools.getLong(node.y, node.x);
            if (outputNode != null && !connectionExists(node, outputNode))
            {
                refreshNodeConnection(outputNode, position, i++);
            }
        }

        return i;
    }

    protected boolean connectionExists(CNode from, CNode to)
    {
        for (GUIElement element : view.children.toArray(new GUIElement[0]))
        {
            if (element instanceof GUIConnector && ((GUIConnector) element).from == from && ((GUIConnector) element).to == to) return true;
        }
        return false;
    }

    protected void refreshNodeConnection(CNode node, long position, int i)
    {
        CNode inputNode = action.EVENT_NODES.get(node.eventName).get(position);
        boolean inputIsConditionNode = inputNode instanceof CNodeTestCondition;

        int i2 = 0;
        boolean found = false;
        for (long p : node.conditionNodePositions)
        {
            if (p == position)
            {
                found = true;
                break;
            }
            i2++;
        }
        if (!found)
        {
            i2 = 0;
            for (long p : node.inputNodePositions)
            {
                if (p == position) break;
                i2++;
            }
        }

        GUIConnector connector = new GUIConnector(this, view, inputNode, node, false);
        GUIConnector connector2 = new GUIConnector(this, view, inputNode, node, true);
        connector.linkMouseActivity(connector2);
        connector2.linkMouseActivity(connector);

        view.add(0, connector);
        view.add(0, connector2);


        String s;
        if (inputIsConditionNode)
        {
            s = TextFormatting.AQUA + "Condition #" + (i2 + 1);
        }
        else
        {
            s = TextFormatting.WHITE + "" + (i2 + 1) + ": ";
            Map.Entry<String, Class>[] requiredInputs = node.getRequiredInputs().entrySet().toArray(new Map.Entry[0]);
            if (i2 < node.getRequiredInputs().size())
            {
                TextFormatting color = Tools.areRelated(inputNode.outputType(), requiredInputs[i2].getValue()) ? TextFormatting.GREEN : TextFormatting.RED;
                TextFormatting color2 = color == TextFormatting.RED ? TextFormatting.LIGHT_PURPLE : TextFormatting.AQUA;

                s += color + requiredInputs[i2].getValue().getSimpleName() + " " + color2 + requiredInputs[i2].getKey();

                if (color == TextFormatting.RED) s += color + " (current input type is " + (inputNode.outputType() == null ? "null" : inputNode.outputType().getSimpleName()) + ")";
            }
            else
            {
                TextFormatting color = Tools.areRelated(inputNode.outputType(), node.getOptionalInputs().getValue()) ? TextFormatting.GREEN : TextFormatting.RED;
                TextFormatting color2 = color == TextFormatting.RED ? TextFormatting.LIGHT_PURPLE : TextFormatting.AQUA;

                s += color + node.getOptionalInputs().getValue().getSimpleName() + " " + color2 + "@" + (i2 + 1 - node.getRequiredInputs().size());

                if (color == TextFormatting.RED) s += color + " (current input type is " + (inputNode.outputType() == null ? "null" : inputNode.outputType().getSimpleName()) + ")";
            }
        }
        connector.setTooltip(s);


        if (!inputIsConditionNode || viewMode == VIEW_MODE_INPUT_ORDER_GLOBAL)
        {
            int ii = viewMode == VIEW_MODE_INPUT_ORDER_GLOBAL ? i : i2;
            GUIText connectorLabel = new GUIText(this, 0, 0, "" + (ii + 1), WHITES[0], WHITES[1], WHITES[2]);

            connectorLabel.linkMouseActivity(connector);
            connector.linkMouseActivity(connectorLabel);

            connectorLabel.linkMouseActivity(connector2);
            connector2.linkMouseActivity(connectorLabel);

            view.add(connectorLabel);
            connectorLabel.setAbsoluteX(connector.absoluteX() + (connector.absoluteWidth() - connectorLabel.absoluteWidth()) * 0.5);
            connectorLabel.setAbsoluteY(connector.absoluteY() + (connector.absoluteHeight() - connectorLabel.absoluteHeight()) * 0.5);


            view.addRemoveChildActions(element ->
            {
                if (element == connector) view.remove(connectorLabel);
                return true;
            });
        }
    }


    @Override
    public String title()
    {
        return event;
    }
}
