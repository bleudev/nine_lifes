package com.bleudev.nine_lifes.util;

import com.mojang.serialization.MapCodec;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static com.bleudev.nine_lifes.NineLifesConst.MOD_ID;

public class RegistryUtils {
    @Contract("_ -> new")
    public static @NotNull Identifier getId(@NotNull String name) {
        return Identifier.of(MOD_ID, name);
    }

    @Contract("_, _ -> new")
    public static @NotNull RegistryEntry<StatusEffect> registerStatusEffect(String name, @NotNull StatusEffect effect) {
        return RegistryEntry.of(Registry.register(Registries.STATUS_EFFECT, getId(name), effect));
    }

    public static <T extends ConsumeEffect> ConsumeEffect.Type<T> registerConsumeEffectType(String name, MapCodec<T> codec, PacketCodec<RegistryByteBuf, T> packetCodec) {
        return Registry.register(Registries.CONSUME_EFFECT_TYPE, getId(name), new ConsumeEffect.Type<>(codec, packetCodec));
    }

    public static RegistryEntry<Potion> registerPotion(String name, StatusEffectInstance... effects) {
        return Registry.registerReference(Registries.POTION, getId(name), new Potion(name, effects));
    }

    public static <T extends Entity> EntityType<T> registerEntity(String name, @NotNull EntityType.Builder<T> type) {
        return Registry.register(
            Registries.ENTITY_TYPE,
            getId(name),
            type.build(RegistryKey.of(RegistryKeys.ENTITY_TYPE,
                                      getId(name)))
        );
    }
}
