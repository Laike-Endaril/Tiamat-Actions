package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.text.GUIFadingText;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.mctools.gui.element.view.GUIPanZoomView;
import com.fantasticsource.mctools.gui.screen.CategorizedTextSelectionGUI;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import com.fantasticsource.tiamatactions.node.*;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.datastructures.Color;
import net.minecraftforge.fml.common.Loader;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class GUINodeView extends GUIPanZoomView
{
    protected static final LinkedHashMap<String, HashSet<String>> NODE_CHOICES_CATEGORIZED = new LinkedHashMap<>();
    protected static final LinkedHashMap<String, Class<? extends CNode>> NODE_CHOICE_CLASSES = new LinkedHashMap<>();
    protected boolean createEditDragging = false;

    static
    {
        addOption("Booleans and Conditions", "Test Condition", CNodeTestCondition.class);
        addOption("Booleans and Conditions", "Boolean", CNodeBoolean.class);
        addOption("Booleans and Conditions", "Comparison", CNodeComparison.class);
        addOption("Booleans and Conditions", "Periodic Boolean", CNodePeriodicBoolean.class);

        addOption("Strings", "Output String", CNodeString.class);
        addOption("Strings", "String Contains", CNodeStringContains.class);
        addOption("Strings", "String Replacement", CNodeStringReplace.class);

        addOption("Entities and Entity Filters", "Source Entity", CNodeSourceEntity.class);
        addOption("Entities and Entity Filters", "Spawn Entity", CNodeSpawnEntity.class);
        addOption("Entities and Entity Filters", "Spawn Entity With NBT", CNodeSpawnEntityWithNBT.class);
        addOption("Entities and Entity Filters", "World Entities", CNodeWorldEntities.class);
        addOption("Entities and Entity Filters", "Entity Filter: In Cube", CNodeEntityFilterInCube.class);
        addOption("Entities and Entity Filters", "Entity Filter: In Sphere", CNodeEntityFilterInSphere.class);
        addOption("Entities and Entity Filters", "Entity Filter: In Cone", CNodeEntityFilterInCone.class);

        addOption("Entity Getters", "Get Entity Attribute Total", CNodeGetAttribute.class);
        addOption("Entity Getters", "Get Entity World", CNodeGetWorld.class);
        addOption("Entity Getters", "Get Entity Dimension", CNodeGetDimension.class);
        addOption("Entity Getters", "Get Entity Eye Position Vector", CNodeGetEyePosition.class);
        addOption("Entity Getters", "Get Entity Position Vector", CNodeGetPosition.class);
        addOption("Entity Getters", "Get Entity Look Vector", CNodeGetLookVector.class);
        addOption("Entity Getters", "Get Entity HP", CNodeGetEntityHP.class);
        addOption("Entity Getters", "Get Entity Variable", CNodeGetEntityVar.class);
        addOption("Entity Getters", "Get Entity Classname", CNodeGetEntityClassname.class);
        addOption("Entity Getters", "Get Entity Motion Vector", CNodeGetEntityMotion.class);
        addOption("Entity Getters", "Get Entity Gravity", CNodeGetEntityGravity.class);
        addOption("Entity Getters", "Get Entity Clip", CNodeGetEntityClip.class);

        addOption("Entity Setters and Functions", "Damage Entity", CNodeDamageEntity.class);
        addOption("Entity Setters and Functions", "Set Entity Position Vector", CNodeSetEntityPosition.class);
        addOption("Entity Setters and Functions", "Set Entity Look Vector", CNodeSetLookVector.class);
        addOption("Entity Setters and Functions", "Set Entity HP", CNodeSetEntityHP.class);
        addOption("Entity Setters and Functions", "Set Entity Variable", CNodeSetEntityVar.class);
        addOption("Entity Setters and Functions", "Set Entity Motion Vector", CNodeSetEntityMotion.class);
        addOption("Entity Setters and Functions", "Set Entity Gravity", CNodeSetEntityGravity.class);
        addOption("Entity Setters and Functions", "Set Entity Clip", CNodeSetEntityClip.class);

        addOption("Itemstacks", "Itemstack", CNodeItemstack.class);
        addOption("Itemstacks", "Get Inventory Itemstack", CNodeGetItemstack.class);
        addOption("Itemstacks", "Set Inventory Itemstack", CNodeSetItemstack.class);
        if (Loader.isModLoaded("tiamatitems"))
        {
            addOption("Itemstacks", "Activate Itemstack", CNodeActivateItemstack.class);
            addOption("Itemstacks", "Deactivate Itemstack", CNodeDeactivateItemstack.class);
            addOption("Itemstacks", "Is Itemstack Active?", CNodeIsItemstackActive.class);
            addOption("Itemstacks", "Get Tiamat Items Parts", CNodeGetTiamatItemsParts.class);
        }

        addOption("NBT", "Get Itemstack NBT", CNodeGetItemstackNBT.class);
        addOption("NBT", "Get Entity NBTCap NBT", CNodeGetEntityNBTCapNBT.class);
        addOption("NBT", "Get NBT Value", CNodeGetNBTValue.class);
        addOption("NBT", "Set NBT Value", CNodeSetNBTValue.class);

        addOption("Blocks", "Blockstate", CNodeBlockstate.class);
        addOption("Blocks", "Get Blockstate", CNodeGetBlockstate.class);
        addOption("Blocks", "Set Blockstate", CNodeSetBlockstate.class);
        addOption("Blocks", "Vector to Block Position", CNodeVectorToBlockPos.class);
        addOption("Blocks", "Blocks in Ray", CNodeBlocksInRay.class);

        addOption("Math", "Output Vector", CNodeVector.class);
        addOption("Math", "Vector Sum", CNodeVectorSum.class);
        addOption("Math", "Vector Difference", CNodeVectorDifference.class);
        addOption("Math", "Ray", CNodeRay.class);
        addOption("Math", "Ray Collision Vector", CNodeRayCollisionVector.class);

        addOption("Arrays", "Get Array Size", CNodeGetArraySize.class);
        addOption("Arrays", "Get From Array", CNodeGetFromArray.class);
        addOption("Arrays", "Add to Array", CNodeAddToArray.class);
        addOption("Arrays", "Remove from Array", CNodeRemoveFromArray.class);
        addOption("Arrays", "Remove Nth from Array", CNodeRemoveNthFromArray.class);
        addOption("Arrays", "Clone Array", CNodeCloneArray.class);
        addOption("Arrays", "Fill Array", CNodeFillArray.class);

        addOption("Lists", "Get List Size", CNodeGetListSize.class);
        addOption("Lists", "Get From List", CNodeGetFromList.class);
        addOption("Lists", "Add to List", CNodeAddToList.class);
        addOption("Lists", "Remove from List", CNodeRemoveFromList.class);
        addOption("Lists", "Remove Nth from List", CNodeRemoveNthFromList.class);
        addOption("Lists", "Clone List", CNodeCloneList.class);
        addOption("Lists", "Clear List", CNodeClearList.class);

        addOption("Actions", "End Action", CNodeEndAction.class);
        addOption("Actions", "Queue Action", CNodeQueueAction.class);
        addOption("Actions", "Run Sub-Action", CNodeSubAction.class);
        addOption("Actions", "Get Action Argument", CNodeGetActionArgument.class);
        addOption("Actions", "Set Action Result", CNodeSetActionResult.class);
        addOption("Actions", "Get Action Variable", CNodeGetActionVar.class);
        addOption("Actions", "Set Action Variable", CNodeSetActionVar.class);
        addOption("Actions", "Get Actions In Queue", CNodeGetActionsInQueue.class);

        addOption("Animation", "Swing Arm", CNodeSwingArm.class);

        addOption("Sounds", "Play Sound at Position", CNodePlaySoundAtPosition.class);
        addOption("Sounds", "Play Sound at Entity Position", CNodePlaySoundAtEntityPosition.class);

        addOption("Misc.", "Null", CNodeNull.class);
        addOption("Misc.", "Evaluate", CNodeEval.class);
        addOption("Misc.", "Run Command", CNodeCommand.class);
        if (Loader.isModLoaded("tiamathud"))
        {
            addOption("Misc.", "Set Custom HUD Data", CNodeSetCustomHUDData.class);
        }

        addOption("Debug", "Show Debug Message", CNodeDebug.class);
    }

    public GUINode tempNode = null;
    public GUITempConnector longConnector = null, shortConnector = null;

    public GUINodeView(GUIScreen screen, double width, double height, GUIElement... subElements)
    {
        super(screen, width, height, subElements);
    }

    public GUINodeView(GUIScreen screen, double x, double y, double width, double height, GUIElement... subElements)
    {
        super(screen, x, y, width, height, subElements);
    }


    protected static void addOption(String category, String name, Class<? extends CNode> nodeClass)
    {
        NODE_CHOICES_CATEGORIZED.computeIfAbsent(category, o -> new HashSet<>()).add(name);
        NODE_CHOICE_CLASSES.put(name, nodeClass);
    }

    protected static LinkedHashMap<String, String[]> getNodeChoicesCategorized()
    {
        LinkedHashMap<String, String[]> result = new LinkedHashMap<>();
        for (Map.Entry<String, HashSet<String>> entry : NODE_CHOICES_CATEGORIZED.entrySet())
        {
            result.put(entry.getKey(), entry.getValue().toArray(new String[0]));
        }
        return result;
    }

    @Override
    public void mouseDrag(int button)
    {
        if (button == TiamatActionsConfig.clientSettings.guiSettings.createEditNodeButton) createEditDragging = true;

        super.mouseDrag(button);
    }

    @Override
    public boolean mouseReleased(int button)
    {
        boolean result = super.mouseReleased(button);

        if (button == TiamatActionsConfig.clientSettings.guiSettings.createEditNodeButton)
        {
            if (createEditDragging)
            {
                createEditDragging = false;
            }
            else
            {
                boolean subElementClicked = false;
                for (GUIElement element : children)
                {
                    if (element.isMouseWithin())
                    {
                        subElementClicked = true;
                        break;
                    }
                }

                if (!subElementClicked)
                {
                    int xx = (int) (viewPxX() + (mouseX() - absoluteX()) / absoluteWidth() * viewPxW()), yy = (int) (viewPxY() + (mouseY() - absoluteY()) / absoluteHeight() * viewPxH());
                    if (!wellSpaced(xx - GUINode.HALF_SIZE, yy - GUINode.HALF_SIZE))
                    {
                        parent.add(new GUIFadingText(screen, x + 5d / screen.pxWidth, y + 5d / screen.pxHeight, "Cannot place a node here: too close to another node", 150, 300, Color.RED));
                    }
                    else
                    {
                        GUIText textElement = new GUIText(screen, "");
                        new CategorizedTextSelectionGUI(textElement, "Select Node Type...", getNodeChoicesCategorized()).addOnClosedActions(() ->
                        {
                            EventEditorGUI gui = (EventEditorGUI) screen;
                            CAction action = gui.action;
                            CNode node = null;

                            Class c = NODE_CHOICE_CLASSES.get(textElement.getText());
                            if (c != null)
                            {
                                try
                                {
                                    node = (CNode) c.newInstance();
                                }
                                catch (InstantiationException | IllegalAccessException e)
                                {
                                    e.printStackTrace();
                                }
                            }

                            if (node != null)
                            {
                                node.actionName = action.name;
                                node.eventName = gui.event;
                                node.x = xx;
                                node.y = yy;

                                action.EVENT_NODES.get(gui.event).put(Tools.getLong(node.y, node.x), node);
                                action.EVENT_ENDPOINT_NODES.get(gui.event).add(node, Tools.getLong(node.y, node.x));

                                double wConversion = 1d / absolutePxWidth(), hConversion = 1d / absolutePxHeight();
                                add(new GUINode(screen, (node.x - GUINode.HALF_SIZE) * wConversion, (node.y - GUINode.HALF_SIZE) * hConversion, node));
                            }
                        });
                    }

                    result = true;
                }
            }
        }

        return result;
    }

    protected boolean wellSpaced(int xx, int yy)
    {
        int ww = absolutePxWidth(), hh = absolutePxHeight();
        for (GUIElement element : children)
        {
            if (!(element instanceof GUINode)) continue;

            if (Tools.distanceSquared(element.x * ww, element.y * hh, xx, yy) < GUINode.MIN_DISTANCE_SQUARED)
            {
                return false;
            }
        }

        return true;
    }
}
