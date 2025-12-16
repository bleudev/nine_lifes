package com.bleudev.nine_lifes

import com.bleudev.nine_lifes.client.config.NineLifesConfig
import com.bleudev.nine_lifes.util.lerp
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import org.joml.Vector2d
import java.util.*
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sin

@Environment(EnvType.CLIENT)
object NineLifesClientData {
    @JvmField
    var lifes = 9

    var armor_stand_hit_event_ticks = 0
    var armor_stand_hit_event_running = false

    var max_whiteness_screen = 0f
    var max_whiteness_screen_ticks = 0
    var whiteness_screen_ticks = 0
    var whiteness_screen_running = false

    var whiteness = 0f
    var redness = 0f
    var amethysm_whiteness = 0f
    var amethysm_purpleness = 0f

    var armor_stand_hit_redness = 0f

    var question_marks = ArrayList<DynamicQuestionMarkInfo>()
    var amethysm_effect_info = AmethysmEffectInfo()
    var charge_effect_info = ChargeEffectInfo()
    var center_heart_info = CenterHeartInfo()

    private fun <T : Player> get_next_heartbeat_rate(player: T?): Int {
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

    fun <T : Player> get_next_heartbeat_time(player: T?): Int {
        val rate = get_next_heartbeat_rate(player)
        if (rate == 0) return 1
        return (1200 / rate.toFloat()).roundToInt()
    }

    fun tick() {
        amethysm_effect_info.tick()
        charge_effect_info.tick()
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
                    if (ticks % 15 == 0) question_marks.add(DynamicQuestionMarkInfo(0.5f, 0.5f, .25f, 40))

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

    class DynamicQuestionMarkInfo(x0p: Float, y0p: Float, private val direction: Vector2d, val duration: Int) {
        private val xy0p: Vector2d = Vector2d(x0p.toDouble(), y0p.toDouble())
        var time: Float = 0f
            private set

        constructor(x0p: Float, y0p: Float, speed: Float, duration: Int) : this(x0p, y0p, Vector2d(0.0), duration) {
            var angle = Random().nextFloat(0f, (2 * Math.PI).toFloat())
            if ((lastAngle > Math.PI && angle > Math.PI) || (lastAngle < Math.PI && angle < Math.PI)) angle =
                (2 * Math.PI - angle).toFloat()
            lastAngle = angle

            this.direction.x = cos(angle.toDouble()) * 2 * speed
            this.direction.y = sin(angle.toDouble()) * speed
        }

        private val fadeIn = 30
        private val fadeOut = 10
        private val maxAlpha = .5f

        private val alpha: Float
            get() {
                if (time <= fadeIn) return (time / fadeIn).lerp(end = maxAlpha)
                if (time <= duration - fadeOut) return maxAlpha
                return (1f - (time - duration + fadeOut) / fadeOut).lerp(end = maxAlpha)
            }

        val offset: Float get() = (time / duration).lerp(end = 5f)

        fun tick(deltaTickProgress: Float): Vec3 {
            val res = Vec3(this.direction.x, this.direction.y, .0)
                .scale((this.time / 20).toDouble())
                .add(this.xy0p.x, this.xy0p.y, this.alpha.toDouble())
            this.time += deltaTickProgress
            return res
        }

        companion object {
            private var lastAngle = 0f
        }
    }

    class CenterHeartInfo {
        private var time = 0f
        private var heartbeatStrength = 0f
        fun tick(deltaTickProgress: Float) {
            this.time += deltaTickProgress
        }

        fun do_heartbeat(strength: Float) {
            if (!NineLifesConfig.heartbeat_enabled) return
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

        val offsetAndScale: Vec3 get() = Vec3(.0, .0, 1.0 + this.currentStrength / 20)

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
}