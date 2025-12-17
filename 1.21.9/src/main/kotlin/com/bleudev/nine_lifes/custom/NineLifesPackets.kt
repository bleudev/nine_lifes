package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.custom.packet.payload.*
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import com.bleudev.nine_lifes.util.createResourceLocation
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

object NineLifesPackets {
    val AFTER_PLAYER_RESPAWN = createResourceLocation("after_player_respawn")
    val ARMOR_STAND_HIT_EVENT = createResourceLocation("armor_stand_hit_event")
    val BETA_MODE_MESSAGE = createResourceLocation("beta_mode_message")
    val JOIN_MESSAGE = createResourceLocation("join_message")
    val START_AMETHYSM_SCREEN = createResourceLocation("start_amethysm_screen")
    val START_CHARGE_SCREEN = createResourceLocation("start_charge_screen")
    val START_WHITENESS_SCREEN = createResourceLocation("start_whiteness_screen")
    val UPDATE_LIFES_COUNT = createResourceLocation("update_lifes_count")

    private fun <T : CustomPacketPayload> registerS2CPacket(packet: PacketPayloadCompanion<T>) =
        PayloadTypeRegistry.playS2C().register(packet.id, packet.codec)

    fun initialize() {
        registerS2CPacket(AfterPlayerRespawn.Companion)
        registerS2CPacket(ArmorStandHitEvent.Companion)
        registerS2CPacket(BetaModeMessage.Companion)
        registerS2CPacket(JoinMessage.Companion)
        registerS2CPacket(StartAmethysmScreen.Companion)
        registerS2CPacket(StartChargeScreen.Companion)
        registerS2CPacket(StartWhitenessScreen.Companion)
        registerS2CPacket(UpdateLifesCount.Companion)
    }
}
