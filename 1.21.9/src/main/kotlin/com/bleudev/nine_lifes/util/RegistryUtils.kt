package com.bleudev.nine_lifes.util

import com.bleudev.nine_lifes.MOD_ID
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

fun createResourceLocation(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)

fun <T : Entity> registerEntity(registryAccess: RegistryAccess, path: String, type: EntityType.Builder<T>): EntityType<T> {
    val key = ResourceKey.create(Registries.ENTITY_TYPE, createResourceLocation(path))
    return Registry.register(
        registryAccess.getOrThrow(Registries.ENTITY_TYPE).value(),
        createResourceLocation(path),
        type.build(key)
    )
}