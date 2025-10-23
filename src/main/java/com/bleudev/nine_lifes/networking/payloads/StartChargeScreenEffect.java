package com.bleudev.nine_lifes.networking.payloads;

import com.bleudev.nine_lifes.networking.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record StartChargeScreenEffect(int duration, float strength) implements CustomPayload {
    public static final Id<StartChargeScreenEffect> ID = new Id<>(Packets.START_CHARGE_SCREEN_EFFECT);
    public static final PacketCodec<RegistryByteBuf, StartChargeScreenEffect> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, StartChargeScreenEffect::duration,
            PacketCodecs.FLOAT, StartChargeScreenEffect::strength,
            StartChargeScreenEffect::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
