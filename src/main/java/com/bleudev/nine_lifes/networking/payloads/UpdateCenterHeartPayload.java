package com.bleudev.nine_lifes.networking.payloads;

import com.bleudev.nine_lifes.networking.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record UpdateCenterHeartPayload(Integer lives) implements CustomPayload {
    public static final CustomPayload.Id<UpdateCenterHeartPayload> ID = new CustomPayload.Id<>(Packets.UPDATE_CENTER_HEART);
    public static final PacketCodec<RegistryByteBuf, UpdateCenterHeartPayload> CODEC = PacketCodec.tuple(
        PacketCodecs.INTEGER, UpdateCenterHeartPayload::lives,
        UpdateCenterHeartPayload::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
