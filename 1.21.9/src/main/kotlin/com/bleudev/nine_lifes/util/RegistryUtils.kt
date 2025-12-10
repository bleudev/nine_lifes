package com.bleudev.nine_lifes.util

import com.bleudev.nine_lifes.MOD_ID
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

fun createResourceLocation(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)

fun registerMobEffect(name: String, effect: MobEffect): Holder<MobEffect> =
    Holder.direct(Registry.register(BuiltInRegistries.MOB_EFFECT, name, effect))

fun <T : Entity> registerEntity(path: String, type: EntityType.Builder<T>): EntityType<T> {
    val key = ResourceKey.create(Registries.ENTITY_TYPE, createResourceLocation(path))
    return Registry.register(BuiltInRegistries.ENTITY_TYPE, path, type.build(key))
}