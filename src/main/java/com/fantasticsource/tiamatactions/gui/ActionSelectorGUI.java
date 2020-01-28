package com.fantasticsource.tiamatactions.gui;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUIGradient;
import com.fantasticsource.mctools.gui.element.other.GUIVerticalScrollbar;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.mctools.gui.element.view.GUIList;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Color;
import net.minecraft.client.Minecraft;

public class ActionSelectorGUI extends GUIScreen
{
    protected GUIList actionList;
    public String selection = null;

    public ActionSelectorGUI(String... list)
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
                GUIText name = new GUIText(screen, "New Action", getIdleColor(Color.WHITE), getHoverColor(Color.WHITE), Color.WHITE);
                if (editable)
                {
                    name.addClickActions(() ->
                    {
                        ActionEditorGUI actionEditorGUI = new ActionEditorGUI(name.getText());
                        actionEditorGUI.addOnClosedActions(() ->
                        {
                            CAction action = actionEditorGUI.getAction();
                            if (action != null)
                            {
                                CAction.allActions.put(actionEditorGUI.saveName, action);
                            }
                        });
                    });
                }
                else
                {
                    name.addClickActions(() ->
                    {
                        selection = name.getText();
                        close();
                    });
                }
                return new GUIElement[]{name};
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
