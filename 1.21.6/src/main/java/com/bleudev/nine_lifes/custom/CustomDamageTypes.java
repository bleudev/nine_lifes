package com.bleudev.nine_lifes.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import static com.bleudev.nine_lifes.Nine_lifes.MOD_ID;

public class CustomDamageTypes {
    public static final RegistryKey<DamageType> AMETHYSM_DAMAGE_TYPE = getDamageType("amethysm");
    public static final RegistryKey<DamageType> CHARGED_AMETHYST_DAMAGE_TYPE = getDamageType("charged_amethyst");
    public static final RegistryKey<DamageType> UNKNOWN_DAMAGE_TYPE = getDamageType("unknown");

    private static RegistryKey<DamageType> getDamageType(String name) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(MOD_ID, name));
    }

    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return world.getDamageSources().create(key);
    }

    public static DamageSource of(Entity entity, RegistryKey<DamageType> key) {
        return entity.getDamageSources().create(key);
    }
}
