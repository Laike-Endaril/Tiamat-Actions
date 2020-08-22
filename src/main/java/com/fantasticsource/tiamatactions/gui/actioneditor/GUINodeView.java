package com.fantasticsource.tiamatactions.gui.actioneditor;

import com.fantasticsource.mctools.gui.GUIScreen;
import com.fantasticsource.mctools.gui.element.GUIElement;
import com.fantasticsource.mctools.gui.element.text.GUIFadingText;
import com.fantasticsource.mctools.gui.element.text.GUIText;
import com.fantasticsource.mctools.gui.element.view.GUIPanZoomView;
import com.fantasticsource.mctools.gui.screen.TextSelectionGUI;
import com.fantasticsource.tiamatactions.action.CAction;
import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import com.fantasticsource.tiamatactions.node.*;
import com.fantasticsource.tools.Tools;
import com.fantasticsource.tools.datastructures.Color;
import net.minecraftforge.fml.common.Loader;

import java.util.LinkedHashMap;

public class GUINodeView extends GUIPanZoomView
{
    protected static final LinkedHashMap<String, Class<? extends CNode>> NODE_CHOICES = new LinkedHashMap<>();
    protected boolean createEditDragging = false;

    static
    {
        String separator = "";

        //Booleans and Conditions
        NODE_CHOICES.put("Test Condition", CNodeTestCondition.class);
        NODE_CHOICES.put("Boolean", CNodeBoolean.class);
        NODE_CHOICES.put("Comparison", CNodeComparison.class);
        NODE_CHOICES.put(separator, null);
        separator += "\r";

        //Static
        NODE_CHOICES.put("Output String", CNodeString.class);
        NODE_CHOICES.put(separator, null);
        separator += "\r";

        //Entities
        NODE_CHOICES.put("Source Entity", CNodeSourceEntity.class);
        NODE_CHOICES.put("World Entities", CNodeWorldEntities.class);
        NODE_CHOICES.put("Entity Filter: In Cube", CNodeEntityFilterInCube.class);
        NODE_CHOICES.put("Entity Filter: In Sphere", CNodeEntityFilterInSphere.class);
        NODE_CHOICES.put("Entity Filter: In Cone", CNodeEntityFilterInCone.class);
        NODE_CHOICES.put(separator, null);
        separator += "\r";

        //Itemstacks
        NODE_CHOICES.put("Itemstack", CNodeItemstack.class);
        NODE_CHOICES.put("Get Inventory Itemstack", CNodeGetItemstack.class);
        NODE_CHOICES.put("Set Inventory Itemstack", CNodeSetItemstack.class);
        NODE_CHOICES.put(separator, null);
        separator += "\r";

        //NBT
        NODE_CHOICES.put("Get Itemstack NBT", CNodeGetItemstackNBT.class);
        NODE_CHOICES.put("Get Entity NBTCap NBT", CNodeGetEntityNBTCapNBT.class);
        NODE_CHOICES.put("Get Sub-NBT", CNodeGetNBTValue.class);
        NODE_CHOICES.put("Set Sub-NBT", CNodeSetNBTValue.class);
        NODE_CHOICES.put(separator, null);
        separator += "\r";

        //Blocks
        NODE_CHOICES.put("Blockstate", CNodeBlockstate.class);
        NODE_CHOICES.put("Get Blockstate", CNodeGetBlockstate.class);
        NODE_CHOICES.put("Set Blockstate", CNodeSetBlockstate.class);
        NODE_CHOICES.put("Vector to Block Position", CNodeVectorToBlockPos.class);
        NODE_CHOICES.put("Blocks in Ray", CNodeBlocksInRay.class);
        NODE_CHOICES.put(separator, null);
        separator += "\r";

        //Trig
        NODE_CHOICES.put("Output Vector", CNodeVector.class);
        NODE_CHOICES.put("Vector Sum", CNodeVectorSum.class);
        NODE_CHOICES.put("Vector Difference", CNodeVectorDifference.class);
        NODE_CHOICES.put("Ray", CNodeRay.class);
        NODE_CHOICES.put("Ray Collision Vector", CNodeRayCollisionVector.class);
        NODE_CHOICES.put(separator, null);
        separator += "\r";

        //Array
        NODE_CHOICES.put("Get From Array", CNodeGetFromArray.class);
        NODE_CHOICES.put("Add to Array", CNodeAddToArray.class);
        NODE_CHOICES.put("Remove from Array", CNodeRemoveFromArray.class);
        NODE_CHOICES.put("Get Array Size", CNodeGetArraySize.class);
        NODE_CHOICES.put(separator, null);
        separator += "\r";

        //Entity
        NODE_CHOICES.put("Damage Entity", CNodeDamageEntity.class);
        NODE_CHOICES.put("Get Entity Attribute Total", CNodeGetAttribute.class);
        NODE_CHOICES.put("Get Entity World", CNodeGetWorld.class);
        NODE_CHOICES.put("Get Entity Dimension", CNodeGetDimension.class);
        NODE_CHOICES.put("Get Entity Eye Position Vector", CNodeGetEyePosition.class);
        NODE_CHOICES.put("Get Entity Position Vector", CNodeGetPosition.class);
        NODE_CHOICES.put("Set Entity Position Vector", CNodeSetPosition.class);
        NODE_CHOICES.put("Get Entity Look Vector", CNodeGetLookVector.class);
        NODE_CHOICES.put("Set Entity Look Vector", CNodeSetLookVector.class);
        NODE_CHOICES.put(separator, null);
        separator += "\r";

        //Actions
        NODE_CHOICES.put("End Action", CNodeEndAction.class);
        NODE_CHOICES.put("Run Sub-Action", CNodeSubAction.class);
        NODE_CHOICES.put("Get Action Argument", CNodeGetActionArgument.class);
        NODE_CHOICES.put("Set Action Result", CNodeSetActionResult.class);
        NODE_CHOICES.put("Queue Action", CNodeQueueAction.class);
        NODE_CHOICES.put("Get Action Variable", CNodeGetActionVar.class);
        NODE_CHOICES.put("Set Action Variable", CNodeSetActionVar.class);
        NODE_CHOICES.put(separator, null);
        separator += "\r";

        //Misc.
        NODE_CHOICES.put("Null", CNodeNull.class);
        NODE_CHOICES.put("Evaluate", CNodeEval.class);
        NODE_CHOICES.put("Run Command", CNodeCommand.class);
        NODE_CHOICES.put(separator, null);
        separator += "\r";

        //API
        int apiCount = 0;
        if (Loader.isModLoaded("tiamathud"))
        {
            apiCount++;
            NODE_CHOICES.put("Set Custom HUD Data", CNodeSetCustomHUDData.class);
        }
        if (apiCount > 0)
        {
            NODE_CHOICES.put(separator, null);
            separator += "\r";
        }

        //Debug
        NODE_CHOICES.put("Show Debug Message", CNodeDebug.class);
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
                        new TextSelectionGUI(textElement, "Select Node Type...", NODE_CHOICES.keySet().toArray(new String[0])).addOnClosedActions(() ->
                        {
                            EventEditorGUI gui = (EventEditorGUI) screen;
                            CAction action = gui.action;
                            CNode node = null;

                            Class c = NODE_CHOICES.get(textElement.getText());
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
