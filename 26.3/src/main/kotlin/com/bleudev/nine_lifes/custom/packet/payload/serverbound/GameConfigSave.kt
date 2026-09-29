package com.bleudev.nine_lifes.custom.packet.payload.serverbound

import com.bleudev.nine_lifes.api.FriendlyStreamCodec
import com.bleudev.nine_lifes.config.NLGameConfigManager
import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class GameConfigSave(val config: NLGameConfigManager.NLGameConfig): CustomPacketPayload {
    companion object : PacketPayloadCompanion<GameConfigSave> {
        override val idLocation = NineLifesPackets.SB_GAME_CONFIG_SAVE
        override val codec: FriendlyStreamCodec<GameConfigSave> = StreamCodec.composite(
            NLGameConfigManager.NLGameConfig.STREAM_CODEC, GameConfigSave::config,
            ::GameConfigSave
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}