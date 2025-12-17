package com.bleudev.nine_lifes.custom.packet.payload

import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class JoinMessage(val lifes: Int) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<JoinMessage> {
        override val idLocation = NineLifesPackets.JOIN_MESSAGE
        override val codec: StreamCodec<RegistryFriendlyByteBuf, JoinMessage> = StreamCodec.composite(
            ByteBufCodecs.INT, JoinMessage::lifes,
            ::JoinMessage
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}