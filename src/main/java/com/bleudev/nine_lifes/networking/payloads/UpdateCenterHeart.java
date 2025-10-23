package com.bleudev.nine_lifes.networking.payloads;

import com.bleudev.nine_lifes.networking.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record UpdateCenterHeart(Integer lives) implements CustomPayload {
    public static final CustomPayload.Id<UpdateCenterHeart> ID = new CustomPayload.Id<>(Packets.UPDATE_CENTER_HEART);
    public static final PacketCodec<RegistryByteBuf, UpdateCenterHeart> CODEC = PacketCodec.tuple(
        PacketCodecs.INTEGER, UpdateCenterHeart::lives,
        UpdateCenterHeart::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
