package com.bleudev.nine_lifes.util

import com.bleudev.nine_lifes.MOD_ID
import com.mojang.serialization.MapCodec
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.consume_effects.ConsumeEffect

fun createIdentifier(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)

fun registerMobEffect(name: String, effect: MobEffect): Holder<MobEffect> =
    Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, createIdentifier(name), effect)

fun <T : ConsumeEffect> registerConsumeEffectType(name: String, codec: MapCodec<T>,
                                                  streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>
): ConsumeEffect.Type<T> = Registry.register(BuiltInRegistries.CONSUME_EFFECT_TYPE,
    createIdentifier(name), ConsumeEffect.Type(codec, streamCodec))

fun <T : Entity> registerEntity(path: String, type: EntityType.Builder<T>): EntityType<T> {
    val key = ResourceKey.create(Registries.ENTITY_TYPE, createIdentifier(path))
    return Registry.register(BuiltInRegistries.ENTITY_TYPE, path, type.build(key))
}