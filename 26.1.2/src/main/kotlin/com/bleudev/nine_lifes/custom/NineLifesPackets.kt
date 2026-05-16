package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.custom.packet.payload.*
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import com.bleudev.nine_lifes.custom.packet.payload.unit.AfterPlayerRespawn
import com.bleudev.nine_lifes.custom.packet.payload.unit.BetaModeMessage
import com.bleudev.nine_lifes.custom.packet.payload.unit.StickGiveHeartScreenEffect
import com.bleudev.nine_lifes.util.createIdentifier
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

object NineLifesPackets {
    val UNIT_AFTER_PLAYER_RESPAWN = createIdentifier("packet/unit/after_player_respawn")
    val UNIT_BETA_MODE_MESSAGE = createIdentifier("packet/unit/beta_mode_message")
    val UNIT_STICK_GIVE_HEART_SCREEN_EFFECT = createIdentifier("packet/unit/stick_give_heart_screen_effect")

    val ARMOR_STAND_HIT_EVENT = createIdentifier("packet/armor_stand_hit_event")
    val BED_SLEEPING_PROBLEM_EVENT = createIdentifier("packet/bed_sleeping_problem_event")
    val JOIN_MESSAGE = createIdentifier("packet/join_message")
    val START_AMETHYSM_SCREEN = createIdentifier("packet/start_amethysm_screen")
    val START_CHARGE_SCREEN = createIdentifier("packet/start_charge_screen")
    val START_WHITENESS_SCREEN = createIdentifier("packet/start_whiteness_screen")
    val UPDATE_LIFES_COUNT = createIdentifier("packet/update_lifes_count")
    val UPDATE_STICK_USED_TICKS = createIdentifier("packet/update_stick_used_ticks")

    private fun <T : CustomPacketPayload> registerS2CPacket(packet: PacketPayloadCompanion<T>) =
        PayloadTypeRegistry.clientboundPlay().register(packet.id, packet.codec)

    fun initialize() {
        registerS2CPacket(AfterPlayerRespawn)
        registerS2CPacket(BetaModeMessage)
        registerS2CPacket(StickGiveHeartScreenEffect)

        registerS2CPacket(ArmorStandHitEvent)
        registerS2CPacket(BedSleepingProblemEvent)
        registerS2CPacket(JoinMessage)
        registerS2CPacket(StartAmethysmScreen)
        registerS2CPacket(StartChargeScreen)
        registerS2CPacket(StartWhitenessScreen)
        registerS2CPacket(UpdateLifesCount)
        registerS2CPacket(UpdateStickUsedTicks)
    }
}
