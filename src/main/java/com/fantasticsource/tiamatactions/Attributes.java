package com.fantasticsource.tiamatactions;

import com.fantasticsource.mctools.betterattributes.BetterAttribute;
import com.fantasticsource.mctools.betterattributes.MultipliedParentsAttribute;
import com.fantasticsource.mctools.betterattributes.ScaledParentsAttribute;
import com.fantasticsource.tools.datastructures.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.fantasticsource.tiamatactions.TiamatActions.MODID;

public class Attributes
{
    public static final BetterAttribute
            BODY_TEMPERATURE = new BetterAttribute(MODID + ".temperature", true, 0), //Custom scale, not C, F, K, etc.  Status effects and damage start at +-100
            COVERAGE = new BetterAttribute(MODID + ".coverage", true, 0), //Based on how much non-destroyed armor you're wearing.  100% with any full set of unbroken armor

    //All stats are balanced around a starting average of 10 and a max of 100 for each base attribute
    STRENGTH = new BetterAttribute(MODID + ".strength", true, 10), //Physical strength/force
            DEXTERITY = new BetterAttribute(MODID + ".dexterity", true, 10), //Physical skill/precision/reflexes
            CONSTITUTION = new BetterAttribute(MODID + ".constitution", true, 10), //Physical integrity/durability/resistance
            MAGICAL_FORCE = new BetterAttribute(MODID + ".magicalForce", true, 10), //Magical strength/force
            MAGICAL_SKILL = new BetterAttribute(MODID + ".magicalSkill", true, 10), //Magical skill/precision/reflexes
            MAGICAL_CONSTITUTION = new BetterAttribute(MODID + ".magicalConstitution", true, 10), //Magical integrity/durability/resistance

    //Meters (blocks) per second; average human RUNNING speed is ~5, world record would be a little over 10.4, both based on a 200m dash
    MOVE_SPEED = new ScaledParentsAttribute(MODID + ".moveSpeed", true, 4.4, new Pair<>(STRENGTH, 0.02), new Pair<>(DEXTERITY, 0.02), new Pair<>(CONSTITUTION, 0.02)).setMCAttribute(SharedMonsterAttributes.MOVEMENT_SPEED, 0.01782642796),

    //As a multiplier; 1 for basic start, 4 for maxed
    ATTACK_SPEED = new ScaledParentsAttribute(MODID + ".attackSpeed", true, 0.7, new Pair<>(STRENGTH, 0.01), new Pair<>(DEXTERITY, 0.02)),

    //Interrupt/knockback/trip force should have active modifiers per-attack/skill
    INTERRUPT_FORCE = new BetterAttribute(MODID + ".interruptForce", true, 0, STRENGTH),
            KNOCKBACK_FORCE = new BetterAttribute(MODID + ".knockbackForce", true, 0, STRENGTH),
            TRIP_FORCE = new BetterAttribute(MODID + ".tripForce", true, 0, STRENGTH),
            PHYSICAL_DAMAGE = new MultipliedParentsAttribute(MODID + ".physicalDamage", true, 0, new Pair<>(STRENGTH, 0.5)),
            SLASH_DAMAGE = new BetterAttribute(MODID + ".slashDamage", true, 0, PHYSICAL_DAMAGE),
            PIERCE_DAMAGE = new BetterAttribute(MODID + ".pierceDamage", true, 0, PHYSICAL_DAMAGE),
            BLUNT_DAMAGE = new BetterAttribute(MODID + ".bluntDamage", true, 0, PHYSICAL_DAMAGE),

    //Many of these will be altered further (often reduced) by modifiers on specific weapons and attacks
    PROJECTILE_ACCURACY = new ScaledParentsAttribute(MODID + ".projectileAccuracy", true, 50, new Pair<>(DEXTERITY, 0.5)),
            FINESSE = new ScaledParentsAttribute(MODID + ".finesse", true, 25, new Pair<>(DEXTERITY, 0.2)),
            BLOCK_CHANCE = new ScaledParentsAttribute(MODID + ".block", true, 50, new Pair<>(DEXTERITY, 0.4)),
            PARRY_CHANCE = new ScaledParentsAttribute(MODID + ".parry", true, 5, new Pair<>(DEXTERITY, 0.1)),
            DODGE_CHANCE = new ScaledParentsAttribute(MODID + ".dodge", true, 50, new Pair<>(DEXTERITY, 0.4)),
            ARMOR_BYPASS_CHANCE = new BetterAttribute(MODID + ".armorBypass", true, 90, DEXTERITY),
            VITAL_STRIKE_CHANCE = new ScaledParentsAttribute(MODID + ".vitalStrike", true, 5, new Pair<>(DEXTERITY, 0.5)),

    HEALTH = new ScaledParentsAttribute(MODID + ".health", true, 278, new Pair<>(CONSTITUTION, 2.22d)).setMCAttribute(SharedMonsterAttributes.MAX_HEALTH, 1), //300 ~ 500 HP
            HEALTH_REGEN = new ScaledParentsAttribute(MODID + ".healthRegen", true, 0, new Pair<>(CONSTITUTION, 0.1)),
            STAMINA = new BetterAttribute(MODID + ".stamina", true, 100, CONSTITUTION),
            STAMINA_REGEN = new ScaledParentsAttribute(MODID + ".staminaRegen", true, 5, new Pair<>(CONSTITUTION, 0.1)),
            STABILITY = new BetterAttribute(MODID + ".stability", true, 90, CONSTITUTION),
            INTERRUPT_STABILITY = new BetterAttribute(MODID + ".interruptStability", true, 0, STABILITY),
            KNOCKBACK_STABILITY = new BetterAttribute(MODID + ".knockbackStability", true, 0, STABILITY),
            TRIP_STABILITY = new BetterAttribute(MODID + ".tripStability", true, 0, STABILITY),

    CHEMICAL_DAMAGE = new MultipliedParentsAttribute(MODID + ".chemicalDamage", true, 0, new Pair<>(MAGICAL_FORCE, 0.5)),
            ACID_DAMAGE = new BetterAttribute(MODID + ".acidDamage", true, 0, CHEMICAL_DAMAGE),
            BIOLOGICAL_DAMAGE = new BetterAttribute(MODID + ".biologicalDamage", true, 0, CHEMICAL_DAMAGE),
            HEALING_DAMAGE = new BetterAttribute(MODID + ".healingDamage", true, 0, BIOLOGICAL_DAMAGE),
            POISON_DAMAGE = new BetterAttribute(MODID + ".poisonDamage", true, 0, BIOLOGICAL_DAMAGE),
            ENERGY_DAMAGE = new MultipliedParentsAttribute(MODID + ".energyDamage", true, 0, new Pair<>(MAGICAL_FORCE, 0.5)),
            ELECTRIC_DAMAGE = new BetterAttribute(MODID + ".electricDamage", true, 0, ENERGY_DAMAGE),
            THERMAL_DAMAGE = new BetterAttribute(MODID + ".thermalDamage", true, 0, ENERGY_DAMAGE),
            HEAT_DAMAGE = new BetterAttribute(MODID + ".heatDamage", true, 0, THERMAL_DAMAGE),
            COLD_DAMAGE = new BetterAttribute(MODID + ".coldDamage", true, 0, THERMAL_DAMAGE),

    CAST_SUCCESS_CHANCE = new BetterAttribute(MODID + ".castSuccess", true, 50, MAGICAL_SKILL),
            DISPEL_CHANCE = new ScaledParentsAttribute(MODID + ".dispel", true, 0, new Pair<>(MAGICAL_SKILL, 0.5)),

    MANA = new BetterAttribute(MODID + ".mana", true, 100, MAGICAL_CONSTITUTION),
            MANA_REGEN = new ScaledParentsAttribute(MODID + ".manaRegen", true, 5, new Pair<>(MAGICAL_CONSTITUTION, 0.1)),

    DEFENSE = new BetterAttribute(MODID + ".defense", true, 0),
            SLASH_DEFENSE = new BetterAttribute(MODID + ".slashDefense", true, 0, DEFENSE),
            PIERCE_DEFENSE = new BetterAttribute(MODID + ".pierceDefense", true, 0, DEFENSE),
            FALL_DEFENSE = new BetterAttribute(MODID + ".fallDefense", true, 0, DEFENSE),
            BLUNT_DEFENSE = new BetterAttribute(MODID + ".bluntDefense", true, 0, FALL_DEFENSE),

    ELEMENTAL_RESISTANCE = new BetterAttribute(MODID + ".elementalResist", true, 0),
            CHEMICAL_RESISTANCE = new BetterAttribute(MODID + ".chemicalResist", true, 0, MAGICAL_CONSTITUTION),
            ACID_RESISTANCE = new BetterAttribute(MODID + ".acidResist", true, 0, CHEMICAL_RESISTANCE),
            BIOLOGICAL_RESISTANCE = new BetterAttribute(MODID + ".biologicalResist", true, 0, CHEMICAL_RESISTANCE),
            HEALING_RESISTANCE = new BetterAttribute(MODID + ".healingResist", true, 0, BIOLOGICAL_RESISTANCE),
            POISON_RESISTANCE = new BetterAttribute(MODID + ".poisonResist", true, 0, BIOLOGICAL_RESISTANCE),
            ENERGY_RESISTANCE = new BetterAttribute(MODID + ".energyResist", true, 0, MAGICAL_CONSTITUTION),
            ELECTRIC_RESISTANCE = new BetterAttribute(MODID + ".electricResist", true, 0, ENERGY_RESISTANCE),
            THERMAL_RESISTANCE = new BetterAttribute(MODID + ".thermalResist", true, 0, ENERGY_RESISTANCE),
            HEAT_RESISTANCE = new BetterAttribute(MODID + ".heatResist", true, 0, THERMAL_RESISTANCE),
            COLD_RESISTANCE = new BetterAttribute(MODID + ".coldResist", true, 0, THERMAL_RESISTANCE),

    //All of these are determined by the attack/skill being used, and may or may not use other attributes depending
    RANGE = new BetterAttribute(MODID + ".range", true, 0), //Determined by the attack/skill being used
            MELEE_RANGE = new BetterAttribute(MODID + ".meleeRange", true, 0, RANGE),
            MIN_MELEE_RANGE = new BetterAttribute(MODID + ".minMeleeRange", true, 0, MELEE_RANGE),
            MAX_MELEE_RANGE = new BetterAttribute(MODID + ".maxMeleeRange", true, 0, MELEE_RANGE),
            PROJECTILE_RANGE = new BetterAttribute(MODID + ".projectileRange", true, 0, RANGE),
            MIN_PROJECTILE_RANGE = new BetterAttribute(MODID + ".minProjectileRange", true, 0, PROJECTILE_RANGE),
            MAX_PROJECTILE_RANGE = new BetterAttribute(MODID + ".maxProjectileRange", true, 0, PROJECTILE_RANGE),

    //All of these are determined by the attack/skill being used, and may or may not use other attributes depending
    MAX_TARGETS = new BetterAttribute(MODID + ".maxTargets", true, 1),
            MAX_MELEE_TARGETS = new BetterAttribute(MODID + ".maxMeleeTargets", true, 0, MAX_TARGETS), //Determined by the attack/skill being used, and possibly strength/dexterity/magical force/magical skill
            MAX_PROJECTILE_TARGETS = new BetterAttribute(MODID + ".maxProjectileTargets", true, 0, MAX_TARGETS), //Determined by the attack/skill being used

    //All of these are determined by the attack/skill being used, and may or may not use other attributes depending
    MAX_MELEE_ANGLE = new BetterAttribute(MODID + ".maxAngle", true, 0),
            MAX_COMBO_LENGTH = new BetterAttribute(MODID + ".maxCombo", true, 1),
            PROJECTILE_SPEED = new BetterAttribute(MODID + ".projectileSpeed", true, 0),
            AOE_TIME = new BetterAttribute(MODID + ".aoeTime", true, 0),

    //Intelligence and Wisdom are terrible gameplay concepts *for players*...they work fine for NPCs, ONLY FOR DECIDING WHETHER AI MAKES GOOD DECISIONS, and you really only need one for that
    //On the other hand, memory is an often-overlooked concept for AI
    AI_INTELLIGENCE = new BetterAttribute(MODID + ".aiIntelligence", true, 0),
            AI_MEMORY = new BetterAttribute(MODID + ".aiMemory", true, 0);

    //Charisma is a terrible gameplay concept no matter what you apply it to, and I'm not using it period.

    public static void init()
    {
        MinecraftForge.EVENT_BUS.register(Attributes.class);
    }

    @SubscribeEvent
    public static void entityJoinWorld(EntityJoinWorldEvent event)
    {
        Entity entity = event.getEntity();
        if (!(entity instanceof EntityLivingBase)) return;


        //(Re)set the base values for attributes with MC children, recalc their totals, and re-apply them
        MOVE_SPEED.setBaseAmount(entity, MOVE_SPEED.defaultBaseAmount);
        HEALTH.setBaseAmount(entity, HEALTH.defaultBaseAmount);
    }
}
