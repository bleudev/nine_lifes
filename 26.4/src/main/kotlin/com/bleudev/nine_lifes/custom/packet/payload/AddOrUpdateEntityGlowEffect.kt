package com.bleudev.nine_lifes.custom.packet.payload

import com.bleudev.nine_lifes.api.render.GlowState
import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.core.UUIDUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import java.util.*

class AddOrUpdateEntityGlowEffect(val uuid: UUID, val glowState: GlowState): CustomPacketPayload {
    companion object : PacketPayloadCompanion<AddOrUpdateEntityGlowEffect> {
        override val idLocation = NineLifesPackets.ADD_OR_UPDATE_ENTITY_GLOW_EFFECT
        override val codec: StreamCodec<RegistryFriendlyByteBuf, AddOrUpdateEntityGlowEffect> = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, AddOrUpdateEntityGlowEffect::uuid,
            GlowState.STREAM_CODEC, AddOrUpdateEntityGlowEffect::glowState,
            ::AddOrUpdateEntityGlowEffect
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}