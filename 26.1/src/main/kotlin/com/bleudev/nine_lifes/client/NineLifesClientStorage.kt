@file:Environment(EnvType.CLIENT)
package com.bleudev.nine_lifes.client

import com.bleudev.nine_lifes.*
import com.bleudev.nine_lifes.client.config.healthRendering
import com.bleudev.nine_lifes.client.config.heartbeatEnabled
import com.bleudev.nine_lifes.util.lerp
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Player
import java.util.*
import kotlin.math.*

var lifes = 9
@JvmField
var stickUsedTicks = 0

private val isInSurvival: Boolean get() = Minecraft.getInstance().player?.gameMode()?.isSurvival == true

// Lifes helper methods
internal val forceHardcoreDeathScreen get() = lifes <= 1 && !should_death_screen_be_white
internal val forceAlwaysDay get() = lifes <= 3 && isInSurvival
val forceHardcore: Boolean get() = healthRendering(lifes)

// Shaders
val shaderRedMajStrength: Float get() =
    max(if (!isInSurvival) 0f
    else when (lifes) {
        0 -> 1f
        1 -> 1f
        2 -> .666f
        3 -> .333f
        else -> 0f
    }, stickUsedTicks.toFloat() / STICK_USED_EFFECT_TICKS)

private enum class Interpolation {
    SIN {
        override fun get(delta: Float): Float = (-2 * sin(delta - .5f).pow(2) + .46f).coerceIn(0f, 1f)
    };

    abstract fun get(delta: Float): Float
    fun get(delta: Float, left: Float, right: Float) = Mth.lerp(get(delta), left, right)

    operator fun invoke(delta: Float): Float = get(delta)
    operator fun invoke(delta: Float, left: Float, right: Float): Float = get(delta, left, right)
}

private val stickAnaglyphEffect: Float get() =
    ((stickUsedTicks + STICK_USED_EFFECT_HEART_GIVE_DELAY).toFloat() / STICK_USED_EFFECT_TICKS).coerceAtMost(1f).takeIf { stickUsedTicks > 0 } ?: 0f

private val anaglyphEffect: Float get() = listOf(
    amethysm_purpleness,
    whiteness,
    Interpolation.SIN(bed_not_safe_event_ticks.toFloat() / NOT_SAFE_ANAGLYPH_EVENT_DURATION).takeIf{bed_not_safe_event_running} ?: 0f,
    stickAnaglyphEffect
).max()
val shaderAnaglyphX: Float get() = anaglyphEffect * 0.01f
val shaderAnaglyphY: Float get() = anaglyphEffect * 0.0025f
val shaderCBlurStrength: Float get() = stickAnaglyphEffect * 4f

var bed_not_safe_event_ticks = 0
var bed_not_safe_event_running = false

var armor_stand_hit_event_ticks = 0
var armor_stand_hit_event_running = false

var max_whiteness_screen = 0f
var max_whiteness_screen_ticks = 0
var whiteness_screen_ticks = 0
var whiteness_screen_running = false

var whiteness = 0f
@JvmField
var should_death_screen_be_white = false
var redness = 0f
var amethysm_whiteness = 0f
var amethysm_purpleness = 0f

var armor_stand_hit_redness = 0f

// Camera effects
val shakeStrength: Float
    get() = shakeSpeed / 2
val shakeSpeed: Float
    get() = stick_purpleness
// Additional overlay effects
var stick_purpleness: Float = 0f
    get() = max(field, ((stickUsedTicks - STICK_USED_EFFECT_TICKS + STICK_USED_EFFECT_SHAKE_TICKS).toFloat() / STICK_USED_EFFECT_SHAKE_TICKS).coerceIn(0f, 1f))
var stick_purpleness_ticks: Int = 0

var amethysm_effect_info = AmethysmEffectInfo()
var charge_effect_info = ChargeEffectInfo()
var center_heart_info = CenterHeartInfo()

private fun <T : Player> getNextHeartbeatRate(player: T?): Int {
    val random = Random()
    var ans = random.nextInt(70, 90)
    if (player != null) {
        ans += floor(player.maxHealth - player.health).toInt() * 2
        if (!player.isAlive) ans -= 9999
    }
    if (lifes == 0) ans -= 9999
    ans -= (9 - lifes) * 5
    return ans.coerceIn(0, Int.MAX_VALUE)
}

var heartbeat_ticks: Int = 0
var max_heartbeat_ticks: Int = 1

fun <T : Player> getNextHeartbeatTime(player: T?): Int {
    val rate = getNextHeartbeatRate(player)
    if (rate == 0) return 1
    return (1200 / rate.toFloat()).roundToInt()
}

fun storageTick() {
    amethysm_effect_info.tick()
    charge_effect_info.tick()
    if (stick_purpleness_ticks > 0) stick_purpleness_ticks--
    stick_purpleness = stick_purpleness_ticks.toFloat() / STICK_PURPLENESS_GIVE_HEART_TICKS
}

class AmethysmEffectInfo {
    private var running = false
    private var ticks = 0
    private var duration = 0

    fun start(duration: Int) {
        running = true
        ticks = 0
        this.duration = duration
    }

    fun tick() {
        if (running) {
            if (ticks < duration) {
                val whitenessStart = 5
                val whitenessEndFromDurationEnd = 10

                if (ticks < whitenessStart) amethysm_whiteness = ticks.toFloat() / whitenessStart
                else if (ticks < duration - whitenessEndFromDurationEnd) amethysm_whiteness =
                    1f - (ticks - whitenessStart).toFloat() / (duration - whitenessStart - whitenessEndFromDurationEnd)

                if (ticks >= duration - whitenessEndFromDurationEnd) amethysm_purpleness =
                    1f - (ticks - duration + whitenessEndFromDurationEnd).toFloat() / whitenessEndFromDurationEnd
                else if (ticks >= whitenessStart) amethysm_purpleness = 1f
                ticks++
            } else {
                running = false
                amethysm_whiteness = 0f
                amethysm_purpleness = 0f
            }
        }
    }
}

class CenterHeartInfo {
    private var time = 0f
    private var heartbeatStrength = 0f
    fun tick(deltaTickProgress: Float) {
        this.time += deltaTickProgress
    }

    fun doHeartbeat(strength: Float) {
        if (!heartbeatEnabled) return
        time = 0f
        heartbeatStrength = strength
    }

    private val currentStrength: Float
        get() {
            if (time > HEARTBEAT_TIME) return 0f
            if (time <= HEARTBEAT_TIME / 2)
                return (time * 2 / HEARTBEAT_TIME).lerp(end = heartbeatStrength)
            return ((time - HEARTBEAT_TIME / 2) * 2 / HEARTBEAT_TIME).lerp(heartbeatStrength, 0f)
        }

    val scale: Float get() = 1f + this.currentStrength / 20

    companion object {
        private const val HEARTBEAT_TIME = 5f
    }
}

class ChargeEffectInfo {
    private var running = false
    private var ticks = 0
    private var duration = 0
    private var strength = 0f

    fun start(duration: Int, strength: Float) {
        this.running = true
        this.ticks = 0
        this.duration = duration
        this.strength = strength
    }

    fun tick() {
        if (running) {
            ticks++
            if (ticks >= duration) running = false
        }
    }

    fun getWhiteness(): Float {
        if (running) {
            val half = duration / 2
            if (ticks <= half) return strength * ticks / half
            return strength - strength * (ticks - half) / half
        }
        return 0f
    }
}