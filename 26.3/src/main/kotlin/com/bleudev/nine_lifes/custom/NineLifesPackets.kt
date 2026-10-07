package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.custom.packet.payload.*
import com.bleudev.nine_lifes.custom.packet.payload.interfaces.PacketPayloadCompanion
import com.bleudev.nine_lifes.custom.packet.payload.serverbound.GameConfigSave
import com.bleudev.nine_lifes.custom.packet.payload.unit.AfterPlayerRespawn
import com.bleudev.nine_lifes.custom.packet.payload.unit.ArmorStandKillEvent
import com.bleudev.nine_lifes.custom.packet.payload.unit.BetaModeMessage
import com.bleudev.nine_lifes.custom.packet.payload.unit.StickGiveHeartScreenEffect
import com.bleudev.nine_lifes.util.createIdentifier
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

object NineLifesPackets {
    val SB_GAME_CONFIG_SAVE = createIdentifier("packet/sb/game_config_save")

    val UNIT_AFTER_PLAYER_RESPAWN = createIdentifier("packet/unit/after_player_respawn")
    val UNIT_ARMOR_STAND_KILL_EVENT = createIdentifier("packet/unit/armor_stand_kill_event")
    val UNIT_BETA_MODE_MESSAGE = createIdentifier("packet/unit/beta_mode_message")
    val UNIT_STICK_GIVE_HEART_SCREEN_EFFECT = createIdentifier("packet/unit/stick_give_heart_screen_effect")

    val ADD_OR_UPDATE_ENTITY_GLOW_EFFECT = createIdentifier("packet/add_or_update_entity_glow_effect")
    val ARMOR_STAND_HIT_EVENT = createIdentifier("packet/armor_stand_hit_event")
    val BED_SLEEPING_PROBLEM_EVENT = createIdentifier("packet/bed_sleeping_problem_event")
    val DISTANCE_UPDATE = createIdentifier("packet/distance_update")
    val OPEN_GAME_CONFIG_EDIT_SCREEN = createIdentifier("packet/open_game_config_edit_screen")
    val GAME_CONFIG_SYNC = createIdentifier("packet/game_config_sync")
    val JOIN_MESSAGE = createIdentifier("packet/join_message")
    val START_AMETHYSM_SCREEN = createIdentifier("packet/start_amethysm_screen")
    val START_CHARGE_SCREEN = createIdentifier("packet/start_charge_screen")
    val START_WHITENESS_SCREEN = createIdentifier("packet/start_whiteness_screen")
    val UPDATE_FORCE_VANILLA_DEATH_SCREEN_STATE = createIdentifier("packet/update_force_vanilla_death_screen_state")
    val UPDATE_LIFES_COUNT = createIdentifier("packet/update_lifes_count")
    val UPDATE_STICK_USED_TICKS = createIdentifier("packet/update_stick_used_ticks")

    private fun <T : CustomPacketPayload> registerC2SPacket(packet: PacketPayloadCompanion<T>) =
        PayloadTypeRegistry.serverboundPlay().register(packet.id, packet.codec)
    private fun <T : CustomPacketPayload> registerS2CPacket(packet: PacketPayloadCompanion<T>) =
        PayloadTypeRegistry.clientboundPlay().register(packet.id, packet.codec)

    fun initialize() {
        registerC2SPacket(GameConfigSave)

        registerS2CPacket(AfterPlayerRespawn)
        registerS2CPacket(ArmorStandKillEvent)
        registerS2CPacket(BetaModeMessage)
        registerS2CPacket(StickGiveHeartScreenEffect)

        registerS2CPacket(AddOrUpdateEntityGlowEffect)
        registerS2CPacket(ArmorStandHitEvent)
        registerS2CPacket(BedSleepingProblemEvent)
        registerS2CPacket(DistanceUpdate)
        registerS2CPacket(OpenGameConfigEditScreen)
        registerS2CPacket(GameConfigSync)
        registerS2CPacket(JoinMessage)
        registerS2CPacket(StartAmethysmScreen)
        registerS2CPacket(StartChargeScreen)
        registerS2CPacket(StartWhitenessScreen)
        registerS2CPacket(UpdateForceVanillaDeathScreenState)
        registerS2CPacket(UpdateLifesCount)
        registerS2CPacket(UpdateStickUsedTicks)
    }
}
