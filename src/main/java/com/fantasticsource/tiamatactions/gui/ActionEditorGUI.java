package com.fantasticsource.tiamatactions.gui;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.other.GUIDarkenedBackground;
import com.fantasticsource.mctools.gui.element.text.GUILabeledTextInput;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.filter.FilterBlacklist;
import com.fantasticsource.tiamatactions.Network;
import com.fantasticsource.tiamatactions.action.CAction;

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
        showUnstacked();


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
                action.name = name.getText();
                navbar.recalc(0);
            }
        });
        root.add(name);
    }

    @Override
    public String title()
    {
        return action.name + " (Action)";
    }

    @Override
    public void onClosed()
    {
        super.onClosed();

        Network.WRAPPER.sendToServer(new Network.SaveActionPacket(initialName, action));
    }
}
