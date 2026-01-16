package com.bleudev.nine_lifes.client

import com.bleudev.nine_lifes.NineLifesClientData.lifes
import com.bleudev.nine_lifes.client.config.NineLifesConfig
import net.minecraft.client.Minecraft
import net.minecraft.util.ARGB
import net.minecraft.world.phys.Vec3
import org.joml.Vector4f

typealias Transformer<T> = (T) -> T

private val clientInSurvivalLikeGameMode: Boolean
    get() = Minecraft.getInstance().player?.gameMode()?.isSurvival ?: true

val fogColor: Transformer<Vector4f>
    get() = { color ->
        if (NineLifesConfig.low_lifes_red_sky && clientInSurvivalLikeGameMode) {
            val target = Vector4f(1f, 0f, 0f, 1f)
            when (lifes) {
                5 -> color.lerp(target, .1f)
                4 -> color.lerp(target, .3f)
                3 -> color.lerp(target, .5f)
                2 -> color.lerp(target, .7f)
                1 -> color.lerp(target, .9f)
                else -> color
            }
        } else color
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