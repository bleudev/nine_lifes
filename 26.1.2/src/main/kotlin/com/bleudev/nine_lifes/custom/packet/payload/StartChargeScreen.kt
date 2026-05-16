package com.bleudev.nine_lifes.custom.packet.payload

import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class StartChargeScreen(val duration: Int, val strength: Float) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<StartChargeScreen> {
        override val idLocation = NineLifesPackets.START_CHARGE_SCREEN
        override val codec: StreamCodec<RegistryFriendlyByteBuf, StartChargeScreen> = StreamCodec.composite(
            ByteBufCodecs.INT, StartChargeScreen::duration,
            ByteBufCodecs.FLOAT, StartChargeScreen::strength,
            ::StartChargeScreen
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}