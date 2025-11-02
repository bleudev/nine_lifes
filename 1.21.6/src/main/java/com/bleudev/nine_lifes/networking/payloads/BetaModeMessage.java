package com.bleudev.nine_lifes.networking.payloads;

import com.bleudev.nine_lifes.networking.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record BetaModeMessage() implements CustomPayload {
    public static final Id<BetaModeMessage> ID = new Id<>(Packets.BETA_MODE_MESSAGE);
    public static final PacketCodec<RegistryByteBuf, BetaModeMessage> CODEC = PacketCodec.unit(new BetaModeMessage());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
