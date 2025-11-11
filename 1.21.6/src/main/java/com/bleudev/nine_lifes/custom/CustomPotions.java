package com.bleudev.nine_lifes.custom;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;

import static com.bleudev.nine_lifes.util.RegistryUtils.registerPotion;
import static net.minecraft.SharedConstants.TICKS_PER_SECOND;

public class CustomPotions {
    public static final RegistryEntry<Potion> AMETHYSM = registerPotion("amethysm", new StatusEffectInstance(CustomEffects.AMETHYSM, 15 * TICKS_PER_SECOND));

    public static void initialize() {}
}
