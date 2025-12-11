package com.bleudev.nine_lifes.api.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity

typealias EntitySpawn = (entity: Entity, level: ServerLevel) -> Unit

object EntitySpawnEvents {
    @JvmField
    val ENTITY_SPAWN: Event<EntitySpawn> = EventFactory.createArrayBacked(EntitySpawn::class.java) { callbacks ->
        { a, b -> for (callback in callbacks) callback(a, b) }
    }
}