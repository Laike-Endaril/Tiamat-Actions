package com.fantasticsource.tiamatactions.gui;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUIGradient;
import com.fantasticsource.mctools.gui.element.other.GUIVerticalScrollbar;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.mctools.gui.element.view.GUIList;
import com.fantasticsource.tiamatactions.Action;
import com.fantasticsource.tools.datastructures.Color;
import net.minecraft.client.Minecraft;

public class ActionBrowserGUI extends GUIScreen
{
    protected GUIList actionList;

    public static void show()
    {
        ActionBrowserGUI gui = new ActionBrowserGUI();
        Minecraft.getMinecraft().displayGuiScreen(gui);


        //Background
        gui.root.add(new GUIGradient(gui, 0, 0, 1, 1, Color.BLACK.copy().setAF(0.85f)));


        //Header
        GUINavbar navbar = new GUINavbar(gui);
        gui.root.add(navbar);


        //List of existing actions
        gui.actionList = new GUIList(gui, true, 0.98, 1 - (navbar.y + navbar.height))
        {
            @Override
            public GUIElement[] newLineDefaultElements()
            {
                GUIText name = new GUIText(screen, "New Action", getIdleColor(Color.WHITE), getHoverColor(Color.WHITE), Color.WHITE);
                name.addClickActions(() ->
                {
                    ActionEditorGUI actionEditorGUI = new ActionEditorGUI(name.getText());
                    actionEditorGUI.addOnClosedActions(() ->
                    {
                        Action action = actionEditorGUI.getAction();
                        if (action != null)
                        {
                            Action.actions.put(actionEditorGUI.saveName, action);
                        }
                    });
                });
                return new GUIElement[]{name};
            }
        };
        GUIVerticalScrollbar scrollbar = new GUIVerticalScrollbar(gui, 0.02, 1 - (navbar.y + navbar.height), Color.GRAY, Color.BLANK, Color.WHITE, Color.BLANK, gui.actionList);
        gui.root.addAll(gui.actionList, scrollbar);

        //Add existing actions
        scrollbar.addRecalcActions(() ->
        {
            gui.actionList.height = 1 - (navbar.y + navbar.height);
            scrollbar.height = 1 - (navbar.y + navbar.height);
        });
    }

    @Override
    public String title()
    {
        return "Action Editor";
    }
}
