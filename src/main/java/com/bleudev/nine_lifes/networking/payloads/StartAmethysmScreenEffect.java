package com.bleudev.nine_lifes.networking.payloads;

import com.bleudev.nine_lifes.networking.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.Range;

public record StartAmethysmScreenEffect(@Range(from = 20, to = Integer.MAX_VALUE) Integer effect_ticks) implements CustomPayload {
    public static final CustomPayload.Id<StartAmethysmScreenEffect> ID = new Id<>(Packets.START_AMETHYSM_SCREEN_EFFECT);
    public static final PacketCodec<RegistryByteBuf, StartAmethysmScreenEffect> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, StartAmethysmScreenEffect::effect_ticks,
            StartAmethysmScreenEffect::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
