package com.bleudev.nine_lifes.networking.payloads;

import com.bleudev.nine_lifes.networking.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;

public record ArmorStandHitEvent(Vec3d pos) implements CustomPayload {
    public static final CustomPayload.Id<ArmorStandHitEvent> ID = new CustomPayload.Id<>(Packets.ARMOR_STAND_HIT_EVENT);
    public static final PacketCodec<RegistryByteBuf, ArmorStandHitEvent> CODEC = PacketCodec.tuple(
            Vec3d.PACKET_CODEC, ArmorStandHitEvent::pos,
            ArmorStandHitEvent::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
