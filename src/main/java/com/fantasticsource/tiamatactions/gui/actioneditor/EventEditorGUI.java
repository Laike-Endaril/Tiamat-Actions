package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.other.GUIGradient;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Color;

public class EventEditorGUI extends GUIScreen
{
    protected CAction action;
    protected String event;

    public EventEditorGUI(CAction action, String event)
    {
        show(action, event);
    }

    protected void show(CAction action, String event)
    {
        show();


        this.action = action;
        this.event = event;


        //Background
        root.add(new GUIGradient(this, 0, 0, 1, 1, Color.BLACK));


        //Header
        GUINavbar navbar = new GUINavbar(this);
        root.add(navbar);
    }

    @Override
    public String title()
    {
        return event;
    }
}
