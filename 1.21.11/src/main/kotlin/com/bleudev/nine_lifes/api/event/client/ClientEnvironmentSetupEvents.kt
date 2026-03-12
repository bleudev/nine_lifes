package com.bleudev.nine_lifes.api.event.client

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import org.joml.Vector4f

@Environment(EnvType.CLIENT)
object ClientEnvironmentSetupEvents {
    @JvmField
    val FOG_COLOR: Event<FogColor> = createLayerEvent(FogColor::class.java)
    @JvmField
    val SKY_COLOR: Event<SkyColor> = createLayerEvent(SkyColor::class.java)
    @JvmField
    val FOG_START: Event<FogStart> = createLayerEvent(FogStart::class.java)
    @JvmField
    val FOG_END: Event<FogEnd> = createLayerEvent(FogEnd::class.java)

    private fun <V> createLayerEvent(type: Class<EventLayer<V>>): Event<EventLayer<V>> =
        EventFactory.createArrayBacked(type) { callbacks -> { original, current ->
            var current = current
            for (callback in callbacks) {
                current = callback(original, current)
            }
            current
        } }


    private typealias EventLayer<T> = (original: T, current: T) -> T

    typealias FogColor = EventLayer<Vector4f>
    typealias SkyColor = EventLayer<Int>
    typealias FogStart = EventLayer<Float>
    typealias FogEnd = EventLayer<Float>
}