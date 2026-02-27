package com.bleudev.nine_lifes.custom.packet.payload

import com.bleudev.nine_lifes.custom.NineLifesPackets
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class BedSleepingProblemEvent(val problem: PacketBedSleepingProblem) : CustomPacketPayload {
    companion object : PacketPayloadCompanion<BedSleepingProblemEvent> {
        override val idLocation = NineLifesPackets.BED_SLEEPING_PROBLEM_EVENT
        override val codec: StreamCodec<RegistryFriendlyByteBuf, BedSleepingProblemEvent> = StreamCodec.composite(
            PacketBedSleepingProblem.CODEC, BedSleepingProblemEvent::problem,
            ::BedSleepingProblemEvent
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = id
}

enum class PacketBedSleepingProblem(private val id: String) {
    NOT_SAFE("bsp_not_safe");

    companion object {
        val CODEC: StreamCodec<RegistryFriendlyByteBuf, PacketBedSleepingProblem> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, PacketBedSleepingProblem::id
        ) { when (it) {
            NOT_SAFE.id -> NOT_SAFE
            else -> throw MatchException(null, null)
        } }
    }
}