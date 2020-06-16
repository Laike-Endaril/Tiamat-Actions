package com.fantasticsource.tiamatactions.gui;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.Namespace;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUIButton;
import com.fantasticsource.mctools.gui.element.other.GUIGradient;
import com.fantasticsource.mctools.gui.element.other.GUIVerticalScrollbar;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.mctools.gui.element.text.GUITextInput;
import com.fantasticsource.mctools.gui.element.text.filter.FilterBlacklist;
import com.fantasticsource.mctools.gui.element.view.GUIList;
import com.fantasticsource.tools.datastructures.Color;
import net.minecraft.client.Minecraft;

public class MainActionEditorGUI extends GUIScreen
{
    protected static final FilterBlacklist ACTION_NAME_FILTER = new FilterBlacklist("None");
    protected GUIList actionList;

    public MainActionEditorGUI(String... list)
    {
        if (Minecraft.getMinecraft().currentScreen instanceof GUIScreen) showStacked(this);
        else Minecraft.getMinecraft().displayGuiScreen(this);


        //Background
        root.add(new GUIGradient(this, 0, 0, 1, 1, Color.BLACK.copy().setAF(0.85f)));


        //Header
        GUINavbar navbar = new GUINavbar(this);
        root.add(navbar);


        //List of existing actions
        actionList = new GUIList(this, true, 0.98, 1 - (navbar.y + navbar.height))
        {
            @Override
            public GUIElement[] newLineDefaultElements()
            {
                Namespace namespace = namespaces.computeIfAbsent("Actions", o -> new Namespace());
                String nameString = namespace.getFirstAvailableNumberedName("Action");

                return new GUIElement[]{
                        GUIButton.newEditButton(screen).addClickActions(() ->
                        {
                            //TODO open editor with editable name and buttons for events
                            //TODO make sure the name field doesn't allow names already in use
                        }),
                        new GUITextInput(screen, nameString, ACTION_NAME_FILTER).setNamespace("Actions")
                };
            }
        };
        GUIVerticalScrollbar scrollbar = new GUIVerticalScrollbar(this, 0.02, 1 - (navbar.y + navbar.height), Color.GRAY, Color.BLANK, Color.WHITE, Color.BLANK, actionList);
        root.addAll(actionList, scrollbar);

        //Add existing actions
        for (String actionName : list)
        {
            GUIList.Line line = actionList.addLine();
            ((GUIText) line.getLineElement(0)).setText(actionName);
        }

        //Add GUI actions
        scrollbar.addRecalcActions(() ->
        {
            actionList.height = 1 - (navbar.y + navbar.height);
            scrollbar.height = 1 - (navbar.y + navbar.height);
        });
    }

    @Override
    public String title()
    {
        return "Actions";
    }
}
