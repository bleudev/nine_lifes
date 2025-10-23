package com.bleudev.nine_lifes.networking.payloads;

import com.bleudev.nine_lifes.networking.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record JoinMessage(Integer lives) implements CustomPayload {
    public static final CustomPayload.Id<JoinMessage> ID = new CustomPayload.Id<>(Packets.JOIN_MESSAGE);
    public static final PacketCodec<RegistryByteBuf, JoinMessage> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, JoinMessage::lives,
            JoinMessage::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
