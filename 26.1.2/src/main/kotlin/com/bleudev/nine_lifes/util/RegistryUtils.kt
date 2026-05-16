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
import net.minecraft.world.item.Item
import net.minecraft.world.item.consume_effects.ConsumeEffect

fun createIdentifier(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)

private fun <T : Any> ResourceKey<Registry<T>>.key(name: String) =
    ResourceKey.create(this, createIdentifier(name))
private fun <V : Any, T : V> Registry<V>.register(id: Identifier, element: T) =
    Registry.register(this, id, element)
private fun <V : Any, T : V> Registry<V>.register(name: String, element: T) =
    register(createIdentifier(name), element)

fun registerMobEffect(name: String, effect: MobEffect): Holder<MobEffect> =
    Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, createIdentifier(name), effect)

fun <T : ConsumeEffect> registerConsumeEffectType(name: String, mCodec: MapCodec<T>, sCodec: StreamCodec<RegistryFriendlyByteBuf, T>): ConsumeEffect.Type<T> =
    BuiltInRegistries.CONSUME_EFFECT_TYPE.register(name, ConsumeEffect.Type(mCodec, sCodec))

fun <T : Entity> registerEntity(path: String, type: EntityType.Builder<T>): EntityType<T> =
    BuiltInRegistries.ENTITY_TYPE.register(Identifier.parse(path) /* For compatibility */, type.build(Registries.ENTITY_TYPE.key(path)))

fun <T : Item> registerItem(name: String, itemFactory: (Item.Properties) -> T, properties: Item.Properties = Item.Properties()): T =
    BuiltInRegistries.ITEM.register(name, itemFactory(properties.setId(Registries.ITEM.key(name))))
