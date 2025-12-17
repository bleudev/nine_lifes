package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.util.createIdentifier
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion

object NineLifesPotions {
    val AMETHYSM: Holder<Potion> = of("amethysm", MobEffectInstance(NineLifesMobEffects.AMETHYSM, 300))

    private fun of(name: String, vararg effects: MobEffectInstance): Holder<Potion> = Registry.registerForHolder(
        BuiltInRegistries.POTION, createIdentifier(name), Potion(name, *effects))

    fun initialize() {}
}