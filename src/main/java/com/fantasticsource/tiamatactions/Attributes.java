package com.fantasticsource.tiamatactions;

import com.fantasticsource.tiamatactions.config.TiamatActionsConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class Attributes
{
    public static LinkedHashMap<String, RangedAttribute> CUSTOM_ATTRIBUTES = null;

    public static void init()
    {
        CUSTOM_ATTRIBUTES = new LinkedHashMap<>();
        for (String attributeString : TiamatActionsConfig.serverSettings.customAttributes)
        {
            CUSTOM_ATTRIBUTES.put(attributeString, new RangedAttribute(null, MODID + "." + attributeString, 0, -Double.MAX_VALUE, Double.MAX_VALUE));
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
//    public static void entityJoin(EntityJoinWorldEvent event)
//    {
//        Entity entity = event.getEntity();
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
