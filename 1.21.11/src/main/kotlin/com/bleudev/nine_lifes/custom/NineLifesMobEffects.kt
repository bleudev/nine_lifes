package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.custom.effect.mob.AmethysmEffect
import com.bleudev.nine_lifes.util.registerMobEffect

object NineLifesMobEffects {
    @JvmField
    val AMETHYSM = registerMobEffect("amethysm", AmethysmEffect())

    fun initialize() {}
}