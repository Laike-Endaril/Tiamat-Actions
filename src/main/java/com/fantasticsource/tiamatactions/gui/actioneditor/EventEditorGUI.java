package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUIGradient;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.node.CNode;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.datastructures.Color;
import net.minecraft.util.text.TextFormatting;

import java.util.Map;

public class EventEditorGUI extends GUIScreen
{
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
    }


    public void refreshNodeConnections()
    {
        for (GUIElement element : view.children.toArray(new GUIElement[0]))
        {
            if (element instanceof GUIConnector) view.remove(element);
        }

        int i;
        for (CNode node : action.EVENT_NODES.get(event).values())
        {
            i = 0;
            for (long position : node.conditionNodePositions)
            {
                refreshNodeConnection(node, position, i++, true);
            }

            i = 0;
            for (long position : node.inputNodePositions)
            {
                refreshNodeConnection(node, position, i++, false);
            }
        }
    }

    protected void refreshNodeConnection(CNode node, long position, int i, boolean inputIsConditionNode)
    {
        CNode inputNode = action.EVENT_NODES.get(node.eventName).get(position);

        GUIConnector connector = new GUIConnector(this, view, inputNode, node, false);
        GUIConnector connector2 = new GUIConnector(this, view, inputNode, node, true);
        connector.linkMouseActivity(connector2);
        connector2.linkMouseActivity(connector);

        view.add(0, connector);
        view.add(0, connector2);


        String s;
        if (inputIsConditionNode)
        {
            s = TextFormatting.AQUA + "Condition #" + (i + 1);
        }
        else
        {
            s = TextFormatting.WHITE + "" + (i + 1) + ": ";
            Map.Entry<String, Class>[] requiredInputs = node.getRequiredInputs().entrySet().toArray(new Map.Entry[0]);
            if (i < node.getRequiredInputs().size())
            {
                TextFormatting color = Tools.areRelated(inputNode.outputType(), requiredInputs[i].getValue()) ? TextFormatting.GREEN : TextFormatting.RED;
                TextFormatting color2 = color == TextFormatting.RED ? TextFormatting.LIGHT_PURPLE : TextFormatting.AQUA;

                s += color + requiredInputs[i].getValue().getSimpleName() + " " + color2 + requiredInputs[i].getKey();

                if (color == TextFormatting.RED) s += color + " (current input type is " + (inputNode.outputType() == null ? "null" : inputNode.outputType().getSimpleName()) + ")";
            }
            else
            {
                TextFormatting color = Tools.areRelated(inputNode.outputType(), node.getOptionalInputs().getValue()) ? TextFormatting.GREEN : TextFormatting.RED;
                TextFormatting color2 = color == TextFormatting.RED ? TextFormatting.LIGHT_PURPLE : TextFormatting.AQUA;

                s += color + node.getOptionalInputs().getValue().getSimpleName() + " " + color2 + "@" + (i + 1 - node.getRequiredInputs().size());

                if (color == TextFormatting.RED) s += color + " (current input type is " + (inputNode.outputType() == null ? "null" : inputNode.outputType().getSimpleName()) + ")";
            }
        }
        connector.setTooltip(s);


        if (!inputIsConditionNode)
        {
            GUIText connectorLabel = new GUIText(this, 0, 0, "" + (i + 1), WHITES[0], WHITES[1], WHITES[2]);

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
