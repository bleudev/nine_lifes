package com.bleudev.nine_lifes.custom.packet.payload.interfaces

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

interface PacketPayloadCompanion<T : CustomPacketPayload> {
    val idLocation: ResourceLocation

    val id: CustomPacketPayload.Type<T>
        get() = CustomPacketPayload.Type(idLocation)
    val codec: StreamCodec<RegistryFriendlyByteBuf, T>
}