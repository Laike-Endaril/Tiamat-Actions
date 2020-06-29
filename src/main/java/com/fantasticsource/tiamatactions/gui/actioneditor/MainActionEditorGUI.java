package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.Namespace;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUIButton;
import com.fantasticsource.mctools.gui.element.other.GUIGradient;
import com.fantasticsource.mctools.gui.element.other.GUIVerticalScrollbar;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.mctools.gui.element.view.GUIList;
import com.fantasticsource.tiamatactions.Network;
import com.fantasticsource.tools.datastructures.Color;

public class MainActionEditorGUI extends GUIScreen
{
    protected GUIList actionList;

    public MainActionEditorGUI(String... list)
    {
        showUnstacked();


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

                GUIText name = new GUIText(screen, nameString);


                return new GUIElement[]{
                        GUIButton.newEditButton(screen).addClickActions(() -> Network.WRAPPER.sendToServer(new Network.RequestOpenActionEditorPacket(name.getText()))),
                        name
                };
            }
        };
        GUIVerticalScrollbar scrollbar = new GUIVerticalScrollbar(this, 0.02, 1 - (navbar.y + navbar.height), Color.GRAY, Color.BLANK, Color.WHITE, Color.BLANK, actionList);
        root.addAll(actionList, scrollbar);

        //Add existing actions
        for (String actionName : list)
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

    @Override
    public String title()
    {
        return "Actions";
    }
}
