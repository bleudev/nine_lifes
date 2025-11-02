package com.bleudev.nine_lifes.custom;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import static com.bleudev.nine_lifes.Nine_lifes.MOD_ID;
import static net.minecraft.SharedConstants.TICKS_PER_SECOND;

public class CustomPotions {
    public static final RegistryEntry<Potion> AMETHYSM = register("amethysm", new StatusEffectInstance(CustomEffects.AMETHYSM, 15 * TICKS_PER_SECOND));

    private static RegistryEntry<Potion> register(String name, StatusEffectInstance... effects) {
        return Registry.registerReference(Registries.POTION, Identifier.of(MOD_ID, name), new Potion(name, effects));
    }

    public static void initialize() {}
}
