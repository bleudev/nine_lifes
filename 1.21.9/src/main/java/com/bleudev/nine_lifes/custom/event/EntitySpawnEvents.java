package com.bleudev.nine_lifes.custom.event;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class EntitySpawnEvents {
    public static final Event<EntitySpawn> ENTITY_SPAWN = EventFactory.createArrayBacked(EntitySpawn.class, callbacks -> ((entity, level) -> {
        for (EntitySpawn callback : callbacks)
            callback.onEntitySpawn(entity, level);
    }));

    @FunctionalInterface
    public interface EntitySpawn {
        void onEntitySpawn(Entity entity, ServerLevel level);
    }
}
