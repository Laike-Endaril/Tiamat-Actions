package com.fantasticsource.tiamatactions;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatactions.TiamatActions.NAME;

public class EntityVars
{
    protected static final LinkedHashMap<Entity, LinkedHashMap<String, Object>> ENTITY_VARS = new LinkedHashMap<>();

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void tick(TickEvent.ServerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END || ENTITY_VARS == null) return;

        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        server.profiler.startSection(NAME + ": EntityVars removeif");
        ENTITY_VARS.entrySet().removeIf(entry ->
        {
            Entity entity = entry.getKey();
            return !entity.isEntityAlive() || (!entity.isAddedToWorld() && entity.isDead);
        });
        server.profiler.endSection();
    }

    public static Object getValue(Entity entity, String key)
    {
        LinkedHashMap<String, Object> map = ENTITY_VARS.get(entity);
        return map == null ? null : map.get(key);
    }

    public static void setValue(Entity entity, String key, Object value)
    {
        ENTITY_VARS.computeIfAbsent(entity, o -> new LinkedHashMap<>()).put(key, value);
    }
}
