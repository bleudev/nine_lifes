package com.bleudev.nine_lifes.client

import com.bleudev.nine_lifes.NineLifesClientData.lifes
import com.bleudev.nine_lifes.NineLifesClientData.stickUsedTicks
import com.bleudev.nine_lifes.STICK_USED_EFFECT_TICKS
import com.bleudev.nine_lifes.client.config.lowLifesRedSkyEnabled
import net.minecraft.client.Minecraft
import net.minecraft.util.ARGB
import net.minecraft.world.phys.Vec3
import org.joml.Vector4f
import kotlin.math.max

typealias Transformer<T> = (T) -> T

private val clientInSurvivalLikeGameMode: Boolean
    get() = Minecraft.getInstance().player?.gameMode()?.isSurvival ?: true

val fogColor: Transformer<Vector4f>
    get() = { color ->
        var lerpAmount: Float = when (lifes) {
            5 -> .1f
            4 -> .3f
            in 0..3 -> 1f
            else -> 0f
        }
        if (stickUsedTicks > 0) lerpAmount = max(lerpAmount, stickUsedTicks.toFloat() / STICK_USED_EFFECT_TICKS)

        if (lowLifesRedSkyEnabled && clientInSurvivalLikeGameMode) {
            val target = Vector4f(1f, 0f, 0f, 1f)
            color.lerp(target, lerpAmount)
        } else color.lerp(Vector4f(1f, 1f, 1f, 1f), lerpAmount)
    }

val skyColor: Transformer<Int>
    get() = { color ->
        val v3 = ARGB.vector3fFromRGB24(color)
        val v4 = fogColor(Vector4f(v3.x, v3.y, v3.z, 1f))
        ARGB.color(Vec3(v4.x.toDouble(), v4.y.toDouble(), v4.z.toDouble()))
    }

val fogStart: Transformer<Float>
    get() = { start ->
        if (clientInSurvivalLikeGameMode)
            when (lifes) {
                5 -> start * .5f
                4 -> start * .4f
                3 -> start * .3f
                2 -> start * .2f
                1 -> start * .1f
                else -> start
            }
        else start
    }

val fogEnd: Transformer<Float>
    get() = { end ->
        if (clientInSurvivalLikeGameMode)
            when (lifes) {
                5 -> end * .5f
                4 -> end * .4f
                3 -> end * .3f
                2 -> end * .2f
                1 -> end * .1f
                else -> end
            }
        else end
    }