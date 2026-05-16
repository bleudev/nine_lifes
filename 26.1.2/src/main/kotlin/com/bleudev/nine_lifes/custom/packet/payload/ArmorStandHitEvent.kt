package com.bleudev.nine_lifes.custom.packet.payload

import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.phys.Vec3

class ArmorStandHitEvent(val pos: Vec3): CustomPacketPayload {
    companion object : PacketPayloadCompanion<ArmorStandHitEvent> {
        override val idLocation = NineLifesPackets.ARMOR_STAND_HIT_EVENT
        override val codec: StreamCodec<RegistryFriendlyByteBuf, ArmorStandHitEvent> = StreamCodec.composite(
            Vec3.STREAM_CODEC, ArmorStandHitEvent::pos,
            ::ArmorStandHitEvent)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}