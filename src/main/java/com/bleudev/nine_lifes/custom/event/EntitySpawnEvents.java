package com.bleudev.nine_lifes.custom.event;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

public class EntitySpawnEvents {
    public static final Event<EntitySpawn> ENTITY_SPAWN = EventFactory.createArrayBacked(EntitySpawn.class, callbacks -> ((entity, world) -> {
        for (EntitySpawn callback : callbacks)
            callback.onEntitySpawn(entity, world);
    }));

    @FunctionalInterface
    public interface EntitySpawn {
        void onEntitySpawn(Entity entity, ServerWorld world);
    }
}
