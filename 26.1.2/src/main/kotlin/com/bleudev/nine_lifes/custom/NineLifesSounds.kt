package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.WSTAND_KICK_EVENT_RADIUS
import com.bleudev.nine_lifes.WSTAND_KILL_EVENT_RADIUS
import com.bleudev.nine_lifes.util.registerSound
import net.minecraft.sounds.SoundEvent

object NineLifesSounds {
    val ENTITY_WANDERING_ARMOR_STAND_HURT = registerSound("entity/wandering_armor_stand/hurt", WSTAND_KICK_EVENT_RADIUS.toFloat())
    val ENTITY_WANDERING_ARMOR_STAND_DEATH = registerSound("entity/wandering_armor_stand/death", WSTAND_KILL_EVENT_RADIUS.toFloat())

    fun all(): List<SoundEvent> = listOf(
        ENTITY_WANDERING_ARMOR_STAND_HURT,
        ENTITY_WANDERING_ARMOR_STAND_DEATH,
    )

    fun initialize() {}
}