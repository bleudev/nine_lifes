package com.bleudev.nine_lifes.client

import com.bleudev.nine_lifes.NineLifesClientData.lifes
import com.bleudev.nine_lifes.api.event.client.ClientEnvironmentSetupEvents
import net.minecraft.client.Minecraft
import org.joml.Vector4f

typealias Transformer<T> = (T) -> T

internal val clientInSurvivalLikeGameMode: Boolean
    get() = Minecraft.getInstance().player?.gameMode()?.isSurvival ?: true

@Deprecated(message = "Use ClientEnvironmentSetupEvents.FOG_COLOR instead. Will be deleted in 3.5", level = DeprecationLevel.ERROR)
val fogColor: Transformer<Vector4f> get() = { ClientEnvironmentSetupEvents.FOG_COLOR.invoker()(it, it) }
@Deprecated(message = "Use ClientEnvironmentSetupEvents.SKY_COLOR instead. Will be deleted in 3.5", level = DeprecationLevel.ERROR)
val skyColor: Transformer<Int> get() = { ClientEnvironmentSetupEvents.SKY_COLOR.invoker()(it, it) }

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