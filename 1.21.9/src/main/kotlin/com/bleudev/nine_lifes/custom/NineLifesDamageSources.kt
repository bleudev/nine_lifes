package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.util.createResourceLocation
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.level.Level

object NineLifesDamageSources {
    private fun key(name: String): ResourceKey<DamageType> =
        ResourceKey.create(Registries.DAMAGE_TYPE, createResourceLocation(name))

    private fun source(world: Level, key: ResourceKey<DamageType>): DamageSource = world.damageSources().source(key)

    private val KEY_AMETHYSM: ResourceKey<DamageType> = key("amethysm")
    private val KEY_CHARGED: ResourceKey<DamageType> = key("charged")
    private val KEY_UNKNOWN: ResourceKey<DamageType> = key("unknown")

    fun amethysm(level: Level): DamageSource = source(level, KEY_AMETHYSM)
    fun charged(level: Level): DamageSource = source(level, KEY_CHARGED)
    fun unknown(level: Level): DamageSource = source(level, KEY_UNKNOWN)
}