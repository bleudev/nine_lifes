package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.WSTAND_KICK_EVENT_RADIUS
import com.bleudev.nine_lifes.WSTAND_KILL_EVENT_RADIUS
import com.bleudev.nine_lifes.util.registerSound

object NineLifesSounds {
    val GLITCH = registerSound("glitch", WSTAND_KICK_EVENT_RADIUS.toFloat())
    val GLITCH2 = registerSound("glitch2", WSTAND_KILL_EVENT_RADIUS.toFloat())

    fun initialize() {}
}