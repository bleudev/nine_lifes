package com.bleudev.nine_lifes.custom.packet.payload

import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class UpdateLifesCount(val lifes: Int) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<UpdateLifesCount> {
        override val idLocation = NineLifesPackets.UPDATE_LIFES_COUNT
        override val codec: StreamCodec<RegistryFriendlyByteBuf, UpdateLifesCount> = StreamCodec.composite(
            ByteBufCodecs.INT, UpdateLifesCount::lifes,
            ::UpdateLifesCount
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}