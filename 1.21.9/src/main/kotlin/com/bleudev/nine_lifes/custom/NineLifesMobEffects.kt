package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.custom.effect.AmethysmEffect
import com.bleudev.nine_lifes.util.registerMobEffect

object NineLifesMobEffects {
    val AMETHYSM = registerMobEffect("amethysm", AmethysmEffect())

    fun initialize() {}
}