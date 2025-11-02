package com.bleudev.nine_lifes.util;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import static com.bleudev.nine_lifes.NineLifesConst.MOD_ID;

public class RegistryUtils {
    public static Identifier get(String name) {
        return Identifier.of(MOD_ID, name);
    }

    public static RegistryEntry<StatusEffect> registerStatusEffect(String name, StatusEffect effect) {
        return RegistryEntry.of(Registry.register(Registries.STATUS_EFFECT, get(name), effect));
    }

    public static <T extends ConsumeEffect> ConsumeEffect.Type<T> registerConsumeEffectType(String name, MapCodec<T> codec, PacketCodec<RegistryByteBuf, T> packetCodec) {
        return Registry.register(Registries.CONSUME_EFFECT_TYPE, get(name), new ConsumeEffect.Type<>(codec, packetCodec));
    }

    public static <T extends Entity> EntityType<T> registerEntity(String path, EntityType.Builder<T> type) {
        RegistryKey<EntityType<?>> registryKey = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MOD_ID, path));
        return Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MOD_ID, path),
            type.build(registryKey)
        );
    }
}
