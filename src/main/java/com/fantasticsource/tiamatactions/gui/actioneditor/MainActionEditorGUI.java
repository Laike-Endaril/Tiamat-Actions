package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUIButton;
import com.fantasticsource.mctools.gui.element.other.GUIGradient;
import com.fantasticsource.mctools.gui.element.other.GUIVerticalScrollbar;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.mctools.gui.element.view.GUIList;
import com.fantasticsource.tiamatactions.Network;
import com.fantasticsource.tools.datastructures.Color;
import scala.actors.threadpool.Arrays;

import java.util.SortedSet;
import java.util.TreeSet;

public class MainActionEditorGUI extends GUIScreen
{
    protected GUIList actionList;

    public MainActionEditorGUI(String... actionNameArray)
    {
        showUnstacked();


        //Background
        root.add(new GUIGradient(this, 0, 0, 1, 1, Color.BLACK.copy().setAF(0.85f)));


        //Header
        GUINavbar navbar = new GUINavbar(this);
        root.add(navbar);


        //List of actions
        actionList = new GUIList(this, true, 0.98, 1 - (navbar.y + navbar.height))
        {
            @Override
            public GUIElement[] newLineDefaultElements()
            {
                String nameString = "Action";

                if (listContainsName(nameString))
                {
                    int i = 2;
                    while (listContainsName(nameString + i)) i++;
                    nameString += i;
                }

                GUIText name = new GUIText(screen, nameString);


                return new GUIElement[]{
                        GUIButton.newEditButton(screen).addClickActions(() -> Network.WRAPPER.sendToServer(new Network.RequestOpenActionEditorPacket(name.getText()))),
                        name
                };
            }
        };
        actionList.setConfirmLineDeletion(true);
        GUIVerticalScrollbar scrollbar = new GUIVerticalScrollbar(this, 0.02, 1 - (navbar.y + navbar.height), Color.GRAY, Color.BLANK, Color.WHITE, Color.BLANK, actionList);
        root.addAll(actionList, scrollbar);

        //Add existing actions
        TreeSet<String> treeSet = new TreeSet<>(Arrays.asList(actionNameArray));
        for (String actionName : treeSet)
        {
            GUIList.Line line = actionList.addLine();
            ((GUIText) line.getLineElement(1)).setText(actionName);
        }

        //Add GUI actions
        scrollbar.addRecalcActions(() ->
        {
            actionList.height = 1 - (navbar.y + navbar.height);
            scrollbar.height = 1 - (navbar.y + navbar.height);
        });
        actionList.addRemoveChildActions(element ->
        {
            if (element instanceof GUIList.Line)
            {
                GUIText name = (GUIText) ((GUIList.Line) element).getLineElement(1);
                Network.WRAPPER.sendToServer(new Network.DeleteActionPacket(name.getText()));
            }
            return true;
        });
    }

    protected boolean listContainsName(String name)
    {
        for (GUIList.Line line : actionList.getLines())
        {
            if (((GUIText) line.getLineElement(1)).getText().equals(name)) return true;
        }
        return false;
    }

    @Override
    public String title()
    {
        return "Actions";
    }
}
