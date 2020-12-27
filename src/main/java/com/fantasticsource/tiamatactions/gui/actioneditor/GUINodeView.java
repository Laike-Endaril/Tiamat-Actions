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
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class GUINodeView extends GUIPanZoomView
{
    protected static CNode[] copiedNodes = new CNode[0];
    protected static int copiedNodesXOffset = 0, copiedNodesYOffset = 0;

    protected static final LinkedHashMap<String, ArrayList<String>> NODE_CHOICES_CATEGORIZED = new LinkedHashMap<>();
    protected static final LinkedHashMap<String, Class<? extends CNode>> NODE_CHOICE_CLASSES = new LinkedHashMap<>();
    protected boolean createEditDragging = false;

    static
    {
        addOption("Booleans and Conditions", "Test Condition", CNodeTestCondition.class);
        addOption("Booleans and Conditions", "Boolean", CNodeBoolean.class);
        addOption("Booleans and Conditions", "Comparison", CNodeComparison.class);
        addOption("Booleans and Conditions", "Periodic Boolean", CNodePeriodicBoolean.class);

        addOption("Strings", "Output String", CNodeString.class);
        addOption("Strings", "Translate String", CNodeTranslateString.class);
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
        addOption("Entity Getters", "Get Potion Effect Level", CNodeGetPotionEffectLevel.class);
        addOption("Entity Getters", "Get Potion Effect Duration", CNodeGetPotionEffectDuration.class);

        addOption("Entity Setters and Functions", "Damage Entity", CNodeDamageEntity.class);
        addOption("Entity Setters and Functions", "Set Entity Position Vector", CNodeSetEntityPosition.class);
        addOption("Entity Setters and Functions", "Set Entity Look Vector", CNodeSetLookVector.class);
        addOption("Entity Setters and Functions", "Set Entity HP", CNodeSetEntityHP.class);
        addOption("Entity Setters and Functions", "Set Entity Variable", CNodeSetEntityVar.class);
        addOption("Entity Setters and Functions", "Set Entity Motion Vector", CNodeSetEntityMotion.class);
        addOption("Entity Setters and Functions", "Set Entity Gravity", CNodeSetEntityGravity.class);
        addOption("Entity Setters and Functions", "Set Entity Clip", CNodeSetEntityClip.class);
        addOption("Entity Setters and Functions", "Apply Potion Effect", CNodeApplyPotionEffect.class);
        addOption("Entity Setters and Functions", "Remove Potion Effect", CNodeRemovePotionEffect.class);
        addOption("Entity Setters and Functions", "Destroy Entity", CNodeDestroyEntity.class);

        addOption("Itemstacks", "Itemstack", CNodeItemstack.class);
        addOption("Itemstacks", "Get Inventory Itemstack", CNodeGetItemstack.class);
        addOption("Itemstacks", "Set Inventory Itemstack", CNodeSetItemstack.class);
        addOption("Itemstacks", "Get Itemstack Attribute Mod Total", CNodeGetItemstackAttributeModTotal.class);
        if (Loader.isModLoaded("tiamatitems"))
        {
            addOption("Itemstacks", "Activate Itemstack", CNodeActivateItemstack.class);
            addOption("Itemstacks", "Deactivate Itemstack", CNodeDeactivateItemstack.class);
            addOption("Itemstacks", "Is Itemstack Active?", CNodeIsItemstackActive.class);
            addOption("Itemstacks", "Get Tiamat Items Parts", CNodeGetTiamatItemsParts.class);
        }

        addOption("NBT", "Get Itemstack NBT", CNodeGetItemstackNBT.class);
        addOption("NBT", "Get Full Entity NBT", CNodeGetFullEntityNBT.class);
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
        addOption("Arrays", "Array Contains", CNodeArrayContains.class);
        addOption("Arrays", "Position in Array", CNodePositionInArray.class);
        addOption("Arrays", "Get From Array", CNodeGetFromArray.class);
        addOption("Arrays", "Add to Array", CNodeAddToArray.class);
        addOption("Arrays", "Remove from Array", CNodeRemoveFromArray.class);
        addOption("Arrays", "Remove Nth from Array", CNodeRemoveNthFromArray.class);
        addOption("Arrays", "Clone Array", CNodeCloneArray.class);
        addOption("Arrays", "Trim Array", CNodeTrimArray.class);
        addOption("Arrays", "Fill Array", CNodeFillArray.class);

        addOption("Lists", "Get List Size", CNodeGetListSize.class);
        addOption("Lists", "List Contains", CNodeListContains.class);
        addOption("Lists", "Position in List", CNodePositionInList.class);
        addOption("Lists", "Get From List", CNodeGetFromList.class);
        addOption("Lists", "Add to List", CNodeAddToList.class);
        addOption("Lists", "Remove from List", CNodeRemoveFromList.class);
        addOption("Lists", "Remove Nth from List", CNodeRemoveNthFromList.class);
        addOption("Lists", "Clone List", CNodeCloneList.class);
        addOption("Lists", "Trim List", CNodeTrimList.class);
        addOption("Lists", "Clear List", CNodeClearList.class);

        addOption("Actions", "End Action", CNodeEndAction.class);
        addOption("Actions", "Queue Action", CNodeQueueAction.class);
        addOption("Actions", "Run Sub-Action", CNodeSubAction.class);
        addOption("Actions", "Get Action Argument", CNodeGetActionArgument.class);
        addOption("Actions", "Set Action Result", CNodeSetActionResult.class);
        addOption("Actions", "Get Action Variable", CNodeGetActionVar.class);
        addOption("Actions", "Set Action Variable", CNodeSetActionVar.class);
        addOption("Actions", "Get Actions In Queue", CNodeGetActionsInQueue.class);
        addOption("Actions", "Get Main Action Name", CNodeGetMainActionName.class);

        addOption("Animation", "Swing Arm", CNodeSwingArm.class);

        addOption("Sounds", "Play Sound at Position", CNodePlaySoundAtPosition.class);
        addOption("Sounds", "Play Sound at Entity Position", CNodePlaySoundAtEntityPosition.class);
        if (Loader.isModLoaded("faeruncharacters"))
        {
            addOption("Sounds", "Get Voice Sound", CNodeGetVoiceSound.class);
        }

        addOption("Java", "Get Class from Object", CNodeGetClassFromObject.class);
        addOption("Java", "Get Class by Name", CNodeGetClassByName.class);
        addOption("Java", "Get Field Value", CNodeGetFieldValue.class);
        addOption("Java", "Set Field Value", CNodeSetFieldValue.class);
        addOption("Java", "Invoke Method", CNodeInvokeMethod.class);

        addOption("Misc.", "Debug", CNodeDebug.class);
        addOption("Misc.", "Comment", CNodeComment.class);
        addOption("Misc.", "Evaluate", CNodeEval.class);
        addOption("Misc.", "Run Command", CNodeCommand.class);
        addOption("Misc.", "Server Tick", CNodeServerTick.class);
        if (Loader.isModLoaded("tiamathud"))
        {
            addOption("Misc.", "Set Custom HUD Data", CNodeSetCustomHUDData.class);
        }
        addOption("Misc.", "Null", CNodeNull.class);
    }

    public GUINode tempNode = null;
    public GUITempConnector longConnector = null, shortConnector = null;

    public GUINodeView(EventEditorGUI screen, double width, double height, GUIElement... subElements)
    {
        super(screen, width, height, subElements);
    }

    public GUINodeView(EventEditorGUI screen, double x, double y, double width, double height, GUIElement... subElements)
    {
        super(screen, x, y, width, height, subElements);
    }


    protected static void addOption(String category, String name, Class<? extends CNode> nodeClass)
    {
        NODE_CHOICES_CATEGORIZED.computeIfAbsent(category, o -> new ArrayList<>()).add(name);
        NODE_CHOICE_CLASSES.put(name, nodeClass);
    }

    public static Class<? extends CNode>[] getNodeClasses()
    {
        return NODE_CHOICE_CLASSES.values().toArray(new Class[0]);
    }

    public static String getNodeCategoryAndName(Class<? extends CNode> cls)
    {
        for (Map.Entry<String, Class<? extends CNode>> entry : NODE_CHOICE_CLASSES.entrySet())
        {
            if (entry.getValue() == cls)
            {
                String name = entry.getKey();
                for (Map.Entry<String, ArrayList<String>> entry2 : NODE_CHOICES_CATEGORIZED.entrySet())
                {
                    if (entry2.getValue().contains(name)) return entry2.getKey() + " -> " + name;
                }
            }
        }
        return null;
    }

    protected static LinkedHashMap<String, String[]> getNodeChoicesCategorized()
    {
        LinkedHashMap<String, String[]> result = new LinkedHashMap<>();
        for (Map.Entry<String, ArrayList<String>> entry : NODE_CHOICES_CATEGORIZED.entrySet())
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

    @Override
    public void keyTyped(char typedChar, int keyCode)
    {
        super.keyTyped(typedChar, keyCode);
        if (GUIScreen.isCtrlKeyDown())
        {
            EventEditorGUI gui = (EventEditorGUI) screen;

            if (keyCode == Keyboard.KEY_C)
            {
                copiedNodes = gui.action.EVENT_NODES.get(gui.event).values().toArray(new CNode[0]);

                copiedNodesXOffset = Integer.MAX_VALUE;
                copiedNodesYOffset = Integer.MAX_VALUE;
                for (CNode node : copiedNodes)
                {
                    if (node.x < copiedNodesXOffset) copiedNodesXOffset = node.x;
                    if (node.y < copiedNodesYOffset) copiedNodesYOffset = node.y;
                }
            }
            else if (keyCode == Keyboard.KEY_X)
            {
                copiedNodes = gui.action.EVENT_NODES.get(gui.event).values().toArray(new CNode[0]);

                copiedNodesXOffset = Integer.MAX_VALUE;
                copiedNodesYOffset = Integer.MAX_VALUE;
                for (CNode node : copiedNodes)
                {
                    if (node.x < copiedNodesXOffset) copiedNodesXOffset = node.x;
                    if (node.y < copiedNodesYOffset) copiedNodesYOffset = node.y;
                }

                for (GUIElement element : children.toArray(new GUIElement[0]))
                {
                    remove(element);
                    if (element instanceof GUINode)
                    {
                        ((GUINode) element).node.delete(gui.action);
                    }
                }
            }
            else if (keyCode == Keyboard.KEY_V)
            {
                int xx = (int) (viewPxX() + (mouseX() - absoluteX()) / absoluteWidth() * viewPxW()), yy = (int) (viewPxY() + (mouseY() - absoluteY()) / absoluteHeight() * viewPxH());
                int xOffset = xx - copiedNodesXOffset, yOffset = yy - copiedNodesYOffset;

                for (CNode nodeToCopy : copiedNodes)
                {
                    CNode node = (CNode) nodeToCopy.copy();

                    node.actionName = gui.action.name;
                    node.eventName = gui.event;
                    node.x += xOffset;
                    node.y += yOffset;

                    for (int i = 0; i < node.conditionNodePositions.size(); i++)
                    {
                        long l = node.conditionNodePositions.get(i);
                        int x = (int) (l & 0xffffffffL);
                        int y = (int) ((l >>> 32) & 0xffffffffL);
                        node.conditionNodePositions.set(i, Tools.getLong(y + yOffset, x + xOffset));
                    }
                    for (int i = 0; i < node.inputNodePositions.size(); i++)
                    {
                        long l = node.inputNodePositions.get(i);
                        int x = (int) (l & 0xffffffffL);
                        int y = (int) ((l >>> 32) & 0xffffffffL);
                        node.inputNodePositions.set(i, Tools.getLong(y + yOffset, x + xOffset));
                    }
                    for (int i = 0; i < node.outputNodePositions.size(); i++)
                    {
                        long l = node.outputNodePositions.get(i);
                        int x = (int) (l & 0xffffffffL);
                        int y = (int) ((l >>> 32) & 0xffffffffL);
                        node.outputNodePositions.set(i, Tools.getLong(y + yOffset, x + xOffset));
                    }

                    gui.action.EVENT_NODES.get(gui.event).put(Tools.getLong(node.y, node.x), node);
                    gui.action.EVENT_ENDPOINT_NODES.get(gui.event).add(node, Tools.getLong(node.y, node.x));

                    double wConversion = 1d / absolutePxWidth(), hConversion = 1d / absolutePxHeight();
                    add(new GUINode(screen, (node.x - GUINode.HALF_SIZE) * wConversion, (node.y - GUINode.HALF_SIZE) * hConversion, node));
                }

                gui.refreshNodeConnections();
            }
        }
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
