package com.fantasticsource.tiamatactions;

import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import com.fantasticsource.tools.Tools;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class Attributes
{
    public static LinkedHashMap<String, RangedAttribute> CUSTOM_ATTRIBUTES = null;

    public static void init()
    {
        MinecraftForge.EVENT_BUS.register(Attributes.class);

        CUSTOM_ATTRIBUTES = new LinkedHashMap<>();
        for (String attributeString : TiamatActionsConfig.serverSettings.customAttributes)
        {
            String[] tokens = Tools.fixedSplit(attributeString, ",");

            String name = tokens[0].trim();
            double defaultValue = tokens.length < 2 ? 0 : Double.parseDouble(tokens[1].trim());
            RangedAttribute parent = tokens.length < 3 ? null : CUSTOM_ATTRIBUTES.get(tokens[2].trim());
            double min = tokens.length < 4 ? -Double.MAX_VALUE : Double.parseDouble(tokens[3].trim());
            double max = tokens.length < 5 ? -Double.MAX_VALUE : Double.parseDouble(tokens[4].trim());

            CUSTOM_ATTRIBUTES.put(name, new RangedAttribute(parent, MODID + "." + name, defaultValue, min, max));
        }
    }

    @SubscribeEvent
    public static void entityConstructing(EntityEvent.EntityConstructing event)
    {
        Entity entity = event.getEntity();
        if (entity instanceof EntityLivingBase)
        {
            EntityLivingBase livingBase = (EntityLivingBase) entity;

            //Add new attributes to entity
            AttributeMap attributeMap = (AttributeMap) livingBase.getAttributeMap();
            for (RangedAttribute attribute : CUSTOM_ATTRIBUTES.values())
            {
                attributeMap.registerAttribute(attribute);
            }
        }
    }

    //Example in case I need to edit attributes' base values for entities at some point
//    @SubscribeEvent(priority = EventPriority.HIGH)
//    public static void entityJoin(EntityJoinWorldEvent eventName)
//    {
//        Entity entity = eventName.getEntity();
//        if (entity instanceof EntityLivingBase)
//        {
//            EntityLivingBase livingBase = (EntityLivingBase) entity;
//
//
//            //Edit entity's base values for attributes
//            AttributeMap attributeMap = (AttributeMap) livingBase.getAttributeMap();
//            attributeMap.getAttributeInstance(MELEE_ANGLE).setBaseValue(MELEE_ANGLE.getDefaultValue());
//        }
//    }
}
