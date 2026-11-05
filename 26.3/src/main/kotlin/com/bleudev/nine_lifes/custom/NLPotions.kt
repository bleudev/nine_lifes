package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.util.createIdentifier
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion

object NLPotions {
    val AMETHYSM: Holder<Potion> = of("amethysm", MobEffectInstance(NLMobEffects.AMETHYSM, 300))
    val INSOMNIA: Holder<Potion> = of("insomnia", MobEffectInstance(NLMobEffects.INSOMNIA, 1200))
    val LONGER_INSOMNIA: Holder<Potion> = of("longer_insomnia", MobEffectInstance(NLMobEffects.INSOMNIA, 3600))

    private fun of(name: String, vararg effects: MobEffectInstance): Holder<Potion> = Registry.registerForHolder(
        BuiltInRegistries.POTION, createIdentifier(name), Potion(name, *effects))

    fun initialize() {}
}