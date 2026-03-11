package com.bleudev.nine_lifes.api.event.client

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.event.EventFactory
import org.joml.Vector4f

@Environment(EnvType.CLIENT)
object ClientEnvironmentSetupEvents {
    @JvmField
    val FOG_COLOR = EventFactory.createArrayBacked(FogColor::class.java) { callbacks -> { original, current ->
        var current = current
        for (callback in callbacks) {
            current = callback(original, current)
        }
        current
    } }

    @JvmField
    val SKY_COLOR = EventFactory.createArrayBacked(SkyColor::class.java) { callbacks -> { original, current ->
        var current = current
        for (callback in callbacks) {
            current = callback(original, current)
        }
        current
    } }

    typealias FogColor = (original: Vector4f, current: Vector4f) -> Vector4f
    typealias SkyColor = (original: Int, current: Int) -> Int
}