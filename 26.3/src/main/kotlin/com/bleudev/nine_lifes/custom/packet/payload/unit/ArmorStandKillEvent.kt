package com.bleudev.nine_lifes.custom.packet.payload.unit

import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class ArmorStandKillEvent : CustomPacketPayload {
    private constructor()

    companion object : PacketPayloadCompanion<ArmorStandKillEvent> {
        val INSTANCE = ArmorStandKillEvent()

        override val idLocation = NineLifesPackets.UNIT_ARMOR_STAND_KILL_EVENT
        override val codec: StreamCodec<RegistryFriendlyByteBuf, ArmorStandKillEvent> = StreamCodec.unit(INSTANCE)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}