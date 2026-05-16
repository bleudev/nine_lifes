package com.bleudev.nine_lifes.custom.packet.payload

import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class StartWhitenessScreen(val duration: Int, val strength: Float) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<StartWhitenessScreen> {
        override val idLocation = NineLifesPackets.START_WHITENESS_SCREEN
        override val codec: StreamCodec<RegistryFriendlyByteBuf, StartWhitenessScreen> = StreamCodec.composite(
            ByteBufCodecs.INT, StartWhitenessScreen::duration,
            ByteBufCodecs.FLOAT, StartWhitenessScreen::strength,
            ::StartWhitenessScreen
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}