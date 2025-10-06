package com.bleudev.nine_lifes.networking.payloads;

import com.bleudev.nine_lifes.networking.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.Range;

public record StartAmethysmScreenEffectPayload(@Range(from = 20, to = Integer.MAX_VALUE) Integer effect_ticks) implements CustomPayload {
    public static final CustomPayload.Id<StartAmethysmScreenEffectPayload> ID = new Id<>(Packets.START_AMETHYSM_SCREEN_EFFECT);
    public static final PacketCodec<RegistryByteBuf, StartAmethysmScreenEffectPayload> CODEC = PacketCodec.tuple(
        PacketCodecs.INTEGER, StartAmethysmScreenEffectPayload::effect_ticks,
        StartAmethysmScreenEffectPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
