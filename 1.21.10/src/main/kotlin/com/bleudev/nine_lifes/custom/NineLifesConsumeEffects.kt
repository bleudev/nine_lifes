package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.custom.effect.consume.AmethysmConsumeEffect
import com.bleudev.nine_lifes.util.registerConsumeEffectType

object NineLifesConsumeEffects {
    val AMETHYSM = registerConsumeEffectType("amethysm_consume_effect", AmethysmConsumeEffect.MAP_CODEC, AmethysmConsumeEffect.STREAM_CODEC)

    fun initialize() {}
}