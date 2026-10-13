package com.bleudev.nine_lifes.custom.packet.payload

import com.bleudev.nine_lifes.api.FriendlyStreamCodec
import com.bleudev.nine_lifes.config.game.NLGameConfigManager
import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class GameConfigSync(val config: NLGameConfigManager.NLGameConfig): CustomPacketPayload {
    companion object : PacketPayloadCompanion<GameConfigSync> {
        override val idLocation = NineLifesPackets.GAME_CONFIG_SYNC
        override val codec: FriendlyStreamCodec<GameConfigSync> = StreamCodec.composite(
            NLGameConfigManager.NLGameConfig.STREAM_CODEC, GameConfigSync::config,
            ::GameConfigSync
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}