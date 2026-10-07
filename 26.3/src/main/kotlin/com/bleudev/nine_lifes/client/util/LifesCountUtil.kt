package com.bleudev.nine_lifes.client.util

import com.bleudev.nine_lifes.MAX_LIFES
import net.minecraft.ChatFormatting

fun getLifesCountTabStyle(lifesCount: Int): ChatFormatting? =
    when (lifesCount) {
        in 6..MAX_LIFES -> ChatFormatting.GREEN
        in 4..5 -> ChatFormatting.YELLOW
        in 1..3 -> ChatFormatting.RED
        else -> null
    }