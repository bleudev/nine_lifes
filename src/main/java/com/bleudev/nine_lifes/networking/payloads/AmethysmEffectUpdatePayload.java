package com.bleudev.nine_lifes.networking.payloads;

import com.bleudev.nine_lifes.networking.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record AmethysmEffectUpdatePayload(Boolean has_effect) implements CustomPayload {
    public static final CustomPayload.Id<AmethysmEffectUpdatePayload> ID = new CustomPayload.Id<>(Packets.AMETHYSM_EFFECT_UPDATE);
    public static final PacketCodec<RegistryByteBuf, AmethysmEffectUpdatePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOLEAN, AmethysmEffectUpdatePayload::has_effect,
            AmethysmEffectUpdatePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
