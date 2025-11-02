package com.bleudev.nine_lifes.custom;

import com.bleudev.nine_lifes.custom.effect.AmethysmEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;

import static com.bleudev.nine_lifes.util.RegistryUtils.registerStatusEffect;

public class CustomEffects {
    public static final RegistryEntry<StatusEffect> AMETHYSM = registerStatusEffect("amethysm", new AmethysmEffect());

    public static void initialize() {}
}
