package com.fantasticsource.tiamatactions.gui;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.other.GUIDarkenedBackground;
import com.fantasticsource.mctools.gui.element.other.GUIVerticalScrollbar;
import com.fantasticsource.mctools.gui.element.text.GUILabeledTextInput;
import com.fantasticsource.mctools.gui.element.text.GUINavbar;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.mctools.gui.element.text.filter.FilterBlacklist;
import com.fantasticsource.mctools.gui.element.text.filter.FilterInt;
import com.fantasticsource.mctools.gui.element.view.GUIList;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tools.datastructures.Color;
import com.fantasticsource.tools.datastructures.ExplicitPriorityQueue;
import net.minecraft.client.Minecraft;

public class ActionEditorGUI extends GUIScreen
{
    protected static FilterBlacklist nameFilter = new FilterBlacklist("None");
    public String saveName = "None";
    protected String initialName;
    protected GUILabeledTextInput name;
    protected GUIList tasks;
    protected boolean reordering = false;

    public ActionEditorGUI(String actionName)
    {
        show(actionName);
    }

    protected void show(String actionName)
    {
        if (Minecraft.getMinecraft().currentScreen instanceof GUIScreen) GUIScreen.showStacked(this);
        else Minecraft.getMinecraft().displayGuiScreen(this);


        initialName = actionName;


        //Background
        root.add(new GUIDarkenedBackground(this));


        //Header
        GUINavbar navbar = new GUINavbar(this);
        root.add(navbar);


        //Name
        name = new GUILabeledTextInput(this, "Action Name: ", actionName, nameFilter);
        name.input.addRecalcActions(() ->
        {
            if (name.valid() && !name.getText().equals(saveName))
            {
                saveName = name.getText();
                navbar.recalc(0);
            }
        });
        root.add(name);


        //List of tasks
        tasks = new GUIList(this, true, 0.98, 1 - (name.y + name.height))
        {
            @Override
            public GUIElement[] newLineDefaultElements()
            {
                GUILabeledTextInput time = new GUILabeledTextInput(screen, "Time: ", "0", FilterInt.INSTANCE);
                time.addEditActions(() ->
                {
                    reorder();
                    tasks.focus(time.parent);
                });

                return new GUIElement[]{new GUIElement(screen, 1, 0), time, new GUIElement(screen, 1, 0), new GUIText(screen, "Task: "), new GUIAction(screen)};
            }

            @Override
            public Line addLine(int index, GUIElement... lineElements)
            {
                super.addLine(index, lineElements);
                Line line = getLine(index);

                reorder();

                root.setActiveRecursive(false);
                ((GUILabeledTextInput) line.getLineElement(1)).input.setActive(true);
                focus(line);

                return line;
            }
        };
        GUIVerticalScrollbar scrollbar = new GUIVerticalScrollbar(this, 0.02, 1 - (name.y + name.height), Color.GRAY, Color.BLANK, Color.WHITE, Color.BLANK, tasks);
        root.addAll(tasks, scrollbar);

        name.addRecalcActions(() ->
        {
            double h = 1 - (name.y + name.height);
            tasks.height = h;
            scrollbar.height = h;
        });
    }

    @Override
    public String title()
    {
        return saveName + " (Action)";
    }

    public CAction getAction()
    {
        if (!name.valid()) return null;

        if (!name.getText().equals(initialName))
        {
            //TODO warn about overwriting other action of same name
            return null;
        }

        //TODO return new action instead of null
        return null;
    }

    protected void reorder()
    {
        if (reordering) return;
        reordering = true;


        GUIList.Line[] lines = tasks.getLines();

        ExplicitPriorityQueue<GUIList.Line> lineQueue = new ExplicitPriorityQueue<>(tasks.lineCount());
        for (GUIList.Line line : lines)
        {
            GUILabeledTextInput time = (GUILabeledTextInput) line.getLineElement(1);
            if (!time.valid())
            {
                reordering = false;
                return;
            }

            lineQueue.add(line, FilterInt.INSTANCE.parse(time.getText()));
        }

        for (GUIList.Line line : lines) tasks.remove(line);

        int offset = tasks.size();
        while (lineQueue.size() > 0) tasks.add(tasks.size() - offset, lineQueue.poll());


        root.recalc(0);


        reordering = false;
    }
}
