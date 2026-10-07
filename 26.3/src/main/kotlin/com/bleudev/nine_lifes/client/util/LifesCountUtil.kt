package com.bleudev.nine_lifes.client.util

import com.bleudev.nine_lifes.MAX_LIFES
import com.bleudev.nine_lifes.client.playersLifesCount
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import java.util.*

private fun getLifesCountTabStyle(lifesCount: Int): ChatFormatting? = when (lifesCount) {
    in 6..MAX_LIFES -> ChatFormatting.GREEN
    in 4..5 -> ChatFormatting.YELLOW
    in 1..3 -> ChatFormatting.RED
    else -> null
}

fun addLifesCountToName(name: Component, id: UUID): Component {
    if (name.string.isEmpty()) return name
    val l = playersLifesCount[id] ?: return name
    val f = getLifesCountTabStyle(l) ?: return name
    return name.copy().append(Component.literal(" ($l)").withStyle(f))
}
