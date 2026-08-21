package com.bleudev.nine_lifes.custom.packet.payload

import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class DistanceUpdate(val toAmethysm: Float, val toCharged: Float) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<DistanceUpdate> {
        override val idLocation = NineLifesPackets.DISTANCE_UPDATE
        override val codec: StreamCodec<RegistryFriendlyByteBuf, DistanceUpdate> = StreamCodec.composite(
            ByteBufCodecs.FLOAT, DistanceUpdate::toAmethysm,
            ByteBufCodecs.FLOAT, DistanceUpdate::toCharged,
            ::DistanceUpdate
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}