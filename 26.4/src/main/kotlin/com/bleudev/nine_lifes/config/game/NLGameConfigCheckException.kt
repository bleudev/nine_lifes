package com.bleudev.nine_lifes.config.game

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

class NLGameConfigCheckException private constructor(val component: Component): Exception(component.string) {
    fun chain(exception: NLGameConfigCheckException): NLGameConfigCheckException {
        return NLGameConfigCheckException(component.copy().append("\n").append(exception.component))
    }

    companion object {
        fun empty(): NLGameConfigCheckException {
            return NLGameConfigCheckException(Component.empty())
        }

        fun of(message: Component): NLGameConfigCheckException {
            return NLGameConfigCheckException(message.copy().withStyle(ChatFormatting.RED))
        }
    }
}