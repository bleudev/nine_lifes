package com.bleudev.nine_lifes.custom.packet.payload.unit

import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class StickGiveHeartScreenEffect : CustomPacketPayload {
    private constructor()

    companion object : PacketPayloadCompanion<StickGiveHeartScreenEffect> {
        val INSTANCE = StickGiveHeartScreenEffect()

        override val idLocation = NineLifesPackets.UNIT_STICK_GIVE_HEART_SCREEN_EFFECT
        override val codec: StreamCodec<RegistryFriendlyByteBuf, StickGiveHeartScreenEffect> = StreamCodec.unit(INSTANCE)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}