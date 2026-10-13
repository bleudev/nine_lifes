package com.bleudev.nine_lifes.custom.packet.payload

import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class UpdateForceVanillaDeathScreenState(val state: Boolean): CustomPacketPayload {
    companion object : PacketPayloadCompanion<UpdateForceVanillaDeathScreenState> {
        override val idLocation = NineLifesPackets.UPDATE_FORCE_VANILLA_DEATH_SCREEN_STATE
        override val codec: StreamCodec<RegistryFriendlyByteBuf, UpdateForceVanillaDeathScreenState> = StreamCodec.composite(
            ByteBufCodecs.BOOL, UpdateForceVanillaDeathScreenState::state,
            ::UpdateForceVanillaDeathScreenState)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}