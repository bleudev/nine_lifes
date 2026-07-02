package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.util.createIdentifier
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.level.Level

object NineLifesDamageTypes {
    private fun key(name: String): ResourceKey<DamageType> =
        ResourceKey.create(Registries.DAMAGE_TYPE, createIdentifier(name))

    private fun source(level: Level, key: ResourceKey<DamageType>): DamageSource = level.damageSources().source(key)

    val AMETHYSM: ResourceKey<DamageType> = key("amethysm")
    val CHARGED_AMETHYST: ResourceKey<DamageType> = key("charged_amethyst")
    val UNKNOWN: ResourceKey<DamageType> = key("unknown")

    @JvmStatic
    fun amethysm(level: Level): DamageSource = source(level, AMETHYSM)
    @JvmStatic
    fun chargedAmethyst(level: Level): DamageSource = source(level, CHARGED_AMETHYST)
    @JvmStatic
    fun unknown(level: Level): DamageSource = source(level, UNKNOWN)
}