package com.fantasticsource.tiamatactions.action;

import net.minecraft.entity.ai.attributes.AttributeModifier;

import java.util.ArrayList;

public class CAdvancedAction extends CAction
{
    public ArrayList<String> categoryTags = new ArrayList<>();
    public boolean forcedCombo = false; //When true, the first action in canComboTo is automatically used next unless another action in canComboTo is queued
    public ArrayList<CAction> canComboTo = new ArrayList<>();

    //See Attributes class, there are many, including damage types dealt
    public ArrayList<AttributeModifier> activeAttributeModifiers = new ArrayList<>();
}
