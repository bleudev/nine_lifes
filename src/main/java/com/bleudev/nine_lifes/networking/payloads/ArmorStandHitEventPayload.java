package com.bleudev.nine_lifes.networking.payloads;

import com.bleudev.nine_lifes.networking.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;

public record ArmorStandHitEventPayload(Vec3d pos) implements CustomPayload {
    public static final CustomPayload.Id<ArmorStandHitEventPayload> ID = new CustomPayload.Id<>(Packets.ARMOR_STAND_HIT_EVENT);
    public static final PacketCodec<RegistryByteBuf, ArmorStandHitEventPayload> CODEC = PacketCodec.tuple(
            Vec3d.PACKET_CODEC, ArmorStandHitEventPayload::pos,
            ArmorStandHitEventPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
