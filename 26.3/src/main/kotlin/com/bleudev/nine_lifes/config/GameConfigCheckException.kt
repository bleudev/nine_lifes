package com.bleudev.nine_lifes.config

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

class GameConfigCheckException private constructor(val component: Component): Exception(component.string) {
    fun chain(exception: GameConfigCheckException): GameConfigCheckException {
        return GameConfigCheckException(component.copy().append("\n").append(exception.component))
    }

    companion object {
        fun empty(): GameConfigCheckException {
            return GameConfigCheckException(Component.empty())
        }

        fun of(message: Component): GameConfigCheckException {
            return GameConfigCheckException(message.copy().withStyle(ChatFormatting.RED))
        }
    }
}