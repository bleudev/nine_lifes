package com.bleudev.nine_lifes.custom.packet.payload

import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import org.jetbrains.annotations.Range

class StartAmethysmScreen(val duration: @Range(from = 20, to = Int.MAX_VALUE.toLong()) Int) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<StartAmethysmScreen> {
        override val idLocation = NineLifesPackets.START_AMETHYSM_SCREEN
        override val codec: StreamCodec<RegistryFriendlyByteBuf, StartAmethysmScreen> = StreamCodec.composite(
            ByteBufCodecs.INT, StartAmethysmScreen::duration,
            ::StartAmethysmScreen
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}