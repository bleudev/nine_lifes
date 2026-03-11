package com.bleudev.nine_lifes.client

import com.bleudev.nine_lifes.api.event.client.ClientEnvironmentSetupEvents
import org.joml.Vector4f

@Deprecated(message = "Use com.bleudev.nine_lifes.api.Transformer instead. Will be deleted in 3.5", level = DeprecationLevel.ERROR)
typealias Transformer<T> = com.bleudev.nine_lifes.api.Transformer<T>

@Deprecated(message = "Use ClientEnvironmentSetupEvents.FOG_COLOR instead. Will be deleted in 3.5", level = DeprecationLevel.ERROR)
val fogColor: com.bleudev.nine_lifes.api.Transformer<Vector4f> get() = { ClientEnvironmentSetupEvents.FOG_COLOR.invoker()(it, it) }
@Deprecated(message = "Use ClientEnvironmentSetupEvents.SKY_COLOR instead. Will be deleted in 3.5", level = DeprecationLevel.ERROR)
val skyColor: com.bleudev.nine_lifes.api.Transformer<Int> get() = { ClientEnvironmentSetupEvents.SKY_COLOR.invoker()(it, it) }
@Deprecated(message = "Use ClientEnvironmentSetupEvents.FOG_START instead. Will be deleted in 3.5", level = DeprecationLevel.ERROR)
val fogStart: com.bleudev.nine_lifes.api.Transformer<Float>
    get() = { ClientEnvironmentSetupEvents.FOG_START.invoker()(it, it) }
@Deprecated(message = "Use ClientEnvironmentSetupEvents.FOG_END instead. Will be deleted in 3.5", level = DeprecationLevel.ERROR)
val fogEnd: com.bleudev.nine_lifes.api.Transformer<Float>
    get() = { ClientEnvironmentSetupEvents.FOG_END.invoker()(it, it) }
