package com.bleudev.nine_lifes.custom.packet.payload

import com.bleudev.nine_lifes.api.FriendlyStreamCodec
import com.bleudev.nine_lifes.config.NLGameConfigManager
import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class GameConfigEditScreen(val config: NLGameConfigManager.NLGameConfig): CustomPacketPayload {
    companion object : PacketPayloadCompanion<GameConfigEditScreen> {
        override val idLocation = NineLifesPackets.GAME_CONFIG_EDIT_SCREEN
        override val codec: FriendlyStreamCodec<GameConfigEditScreen> = StreamCodec.composite(
            NLGameConfigManager.NLGameConfig.STREAM_CODEC, GameConfigEditScreen::config,
            ::GameConfigEditScreen
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}