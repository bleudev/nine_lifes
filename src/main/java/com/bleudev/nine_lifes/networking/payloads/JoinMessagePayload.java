package com.bleudev.nine_lifes.networking.payloads;

import com.bleudev.nine_lifes.networking.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record JoinMessagePayload(Integer lives) implements CustomPayload {
    public static final CustomPayload.Id<JoinMessagePayload> ID = new CustomPayload.Id<>(Packets.JOIN_MESSAGE);
    public static final PacketCodec<RegistryByteBuf, JoinMessagePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, JoinMessagePayload::lives,
            JoinMessagePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
