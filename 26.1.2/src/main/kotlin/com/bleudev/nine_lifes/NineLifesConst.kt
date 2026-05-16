package com.bleudev.nine_lifes

import com.bleudev.nine_lifes.util.helper.PlatformHelper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

const val MOD_ID = "nine_lifes"
val NAME = PlatformHelper.getModName(MOD_ID)
val AUTHOR = PlatformHelper.getModAuthorsNames(MOD_ID).getOrNull(0) ?: "Unknown"
val VERSION = PlatformHelper.getModVersion(MOD_ID, "+")
const val GITHUB_LINK = "https://github.com/bleudev/nine_lifes"
const val ISSUES_LINK = "https://github.com/bleudev/nine_lifes/issues"
const val MODRINTH_LINK = "https://modrinth.com/mod/nine_lifes"

const val MAX_LIFES: Int = 9
const val WANDERING_ARMOR_STAND_SPAWN_CHANCE = 0.5f
const val LIGHTNING_CHARGING_RADIUS = 1
const val CHARGE_SCREEN_EFFECT_RADIUS_MIN = 3
const val CHARGE_SCREEN_EFFECT_RADIUS_MAX = 20
const val CHARGE_SCREEN_MAX_STRENGTH = 0.5
const val CHARGE_SCREEN_DURATION = 6
const val STICK_USED_EFFECT_TICKS = 220
const val STICK_USED_EFFECT_HEART_GIVE_DELAY = 20
const val STICK_USED_EFFECT_SHAKE_TICKS = 15
const val STICK_USED_EFFECT_MAX_HEALTH_TAKE = 10
const val STICK_PURPLENESS_GIVE_HEART_TICKS = 15
const val NOT_SAFE_ANAGLYPH_EVENT_DURATION = 60

fun isInBetaMode(): Boolean = VERSION.endsWith("_beta")

val LOGGER: Logger = LoggerFactory.getLogger(NAME)
