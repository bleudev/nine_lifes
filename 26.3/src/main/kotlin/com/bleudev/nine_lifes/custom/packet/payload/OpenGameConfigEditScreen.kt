package com.bleudev.nine_lifes.custom.packet.payload

import com.bleudev.nine_lifes.api.FriendlyStreamCodec
import com.bleudev.nine_lifes.config.game.NLGameConfigManager
import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class OpenGameConfigEditScreen(val config: NLGameConfigManager.NLGameConfig): CustomPacketPayload {
    companion object : PacketPayloadCompanion<OpenGameConfigEditScreen> {
        override val idLocation = NineLifesPackets.OPEN_GAME_CONFIG_EDIT_SCREEN
        override val codec: FriendlyStreamCodec<OpenGameConfigEditScreen> = StreamCodec.composite(
            NLGameConfigManager.NLGameConfig.STREAM_CODEC, OpenGameConfigEditScreen::config,
            ::OpenGameConfigEditScreen
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}