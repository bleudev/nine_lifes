package com.bleudev.nine_lifes.custom.packet.payload.unit

import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class AfterPlayerRespawn : CustomPacketPayload {
    private constructor()

    companion object : PacketPayloadCompanion<AfterPlayerRespawn> {
        val INSTANCE = AfterPlayerRespawn()

        override val idLocation = NineLifesPackets.UNIT_AFTER_PLAYER_RESPAWN
        override val codec: StreamCodec<RegistryFriendlyByteBuf, AfterPlayerRespawn> = StreamCodec.unit(INSTANCE)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}