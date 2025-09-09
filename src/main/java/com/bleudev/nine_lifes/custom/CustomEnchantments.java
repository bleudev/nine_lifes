package com.bleudev.nine_lifes.custom;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import static com.bleudev.nine_lifes.Nine_lifes.MOD_ID;

public class CustomEnchantments {
    public static final RegistryKey<Enchantment> CHARGE = of("charge");

    private static RegistryKey<Enchantment> of(String name) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, name));
    }

    public static void initialize() {
    }
}
