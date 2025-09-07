package com.bleudev.nine_lifes.util;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import static com.bleudev.nine_lifes.Nine_lifes.MOD_ID;

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
}
