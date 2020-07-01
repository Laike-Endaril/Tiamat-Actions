package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUIDarkenedBackground;
import com.fantasticsource.mctools.gui.element.text.*;
import com.fantasticsource.mctools.gui.element.text.filter.FilterBlacklist;
import com.fantasticsource.tiamatactions.Network;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.node.CNode;
import net.minecraft.util.text.TextFormatting;

import java.util.LinkedHashMap;

public class ActionEditorGUI extends GUIScreen
{
    protected String initialName;
    protected CAction action;
    protected GUILabeledTextInput name;

    public ActionEditorGUI(CAction action, String... otherActionNames)
    {
        show(action, otherActionNames);
    }

    protected void show(CAction action, String... otherActionNames)
    {
        show();
        drawStack = false;


        initialName = action.name;
        this.action = action;


        //Background
        root.add(new GUIDarkenedBackground(this));


        //Header
        GUINavbar navbar = new GUINavbar(this);
        root.add(navbar);


        //Name
        name = new GUILabeledTextInput(this, "Action Name: ", action.name, new FilterBlacklist(otherActionNames));
        name.input.addRecalcActions(() ->
        {
            if (name.valid() && !name.getText().equals(action.name))
            {
                String n = name.getText();
                action.name = n;
                for (LinkedHashMap<Long, CNode> map : action.EVENT_NODES.values())
                {
                    for (CNode node : map.values()) node.actionName = n;
                }
                navbar.recalc(0);
            }
        });
        root.addAll(
                name,
                new GUITextSpacer(this),
                new GUIText(this, TextFormatting.GOLD + "Events...")
        );

        //Events
        for (String event : action.EVENT_NODES.keySet())
        {
            root.addAll(
                    new GUIElement(this, 1, 0),
                    new GUITextButton(this, event).addClickActions(() -> new EventEditorGUI(action, event))
            );
        }
    }

    @Override
    public String title()
    {
        return action.name;
    }

    @Override
    public void onClosed()
    {
        super.onClosed();

        Network.WRAPPER.sendToServer(new Network.SaveActionPacket(initialName, action));
    }
}
