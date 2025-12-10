package com.bleudev.nine_lifes.custom.packet.payload

import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class BetaModeMessage() : CustomPacketPayload {
    companion object : PacketPayloadCompanion<BetaModeMessage> {
        override val idLocation = NineLifesPackets.BETA_MODE_MESSAGE
        override val codec: StreamCodec<RegistryFriendlyByteBuf, BetaModeMessage> = StreamCodec.unit(
            BetaModeMessage())
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}