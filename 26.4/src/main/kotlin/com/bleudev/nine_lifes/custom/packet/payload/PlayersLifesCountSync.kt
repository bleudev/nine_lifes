package com.bleudev.nine_lifes.custom.packet.payload

import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.core.UUIDUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import java.util.*

class PlayersLifesCountSync(val playersLifesCount: Map<UUID, Int>): CustomPacketPayload {
    companion object : PacketPayloadCompanion<PlayersLifesCountSync> {
        override val idLocation = NineLifesPackets.PLAYERS_LIFES_COUNT_SYNC
        override val codec: StreamCodec<RegistryFriendlyByteBuf, PlayersLifesCountSync> = StreamCodec.composite(
            ByteBufCodecs.map(::HashMap, UUIDUtil.STREAM_CODEC, ByteBufCodecs.INT), PlayersLifesCountSync::playersLifesCount,
            ::PlayersLifesCountSync
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}