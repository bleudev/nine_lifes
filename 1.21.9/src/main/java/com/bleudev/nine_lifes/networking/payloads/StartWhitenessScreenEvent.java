package com.bleudev.nine_lifes.networking.payloads;

import com.bleudev.nine_lifes.networking.Packets;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record StartWhitenessScreenEvent(Integer duration, Float max_whiteness) implements CustomPayload {
    public static final CustomPayload.Id<StartWhitenessScreenEvent> ID = new CustomPayload.Id<>(Packets.START_WHITENESS_SCREEN_EVENT);
    public static final PacketCodec<RegistryByteBuf, StartWhitenessScreenEvent> CODEC = PacketCodec.tuple(
        PacketCodecs.INTEGER, StartWhitenessScreenEvent::duration,
        PacketCodecs.FLOAT, StartWhitenessScreenEvent::max_whiteness,
        StartWhitenessScreenEvent::new
    );

    public StartWhitenessScreenEvent(Integer duration) {
        this(duration, 1f);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
