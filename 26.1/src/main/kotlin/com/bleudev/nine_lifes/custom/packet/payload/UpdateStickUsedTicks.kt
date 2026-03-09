package com.bleudev.nine_lifes.custom.packet.payload

import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class UpdateStickUsedTicks(val ticks: Int) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<UpdateStickUsedTicks> {
        override val idLocation = NineLifesPackets.UPDATE_STICK_USED_TICKS
        override val codec: StreamCodec<RegistryFriendlyByteBuf, UpdateStickUsedTicks> = StreamCodec.composite(
            ByteBufCodecs.INT, UpdateStickUsedTicks::ticks,
            ::UpdateStickUsedTicks
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}