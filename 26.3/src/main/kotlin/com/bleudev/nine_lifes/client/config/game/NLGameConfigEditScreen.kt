package com.bleudev.nine_lifes.client.config.game

import com.bleudev.nine_lifes.GAME_CONFIG_YACL_ID
import com.bleudev.nine_lifes.config.NLGameConfigManager
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder
import dev.isxander.yacl3.dsl.YetAnotherConfigLib
import dev.isxander.yacl3.dsl.descriptionBuilder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component

object NLGameConfigEditScreen {
    fun generate(config: NLGameConfigManager.NLGameConfig, parent: Screen?, saveFunc: (NLGameConfigManager.NLGameConfig) -> Unit): Screen {
        val d = NLGameConfigManager.NLGameConfig()
        val c = config.with() // Copy
        return YetAnotherConfigLib(GAME_CONFIG_YACL_ID) {
            save { saveFunc(c) }

            categories.register("general") {
                rootOptions.register("disable_wstands") {
                    controller { BooleanControllerBuilder.create(it).coloured(true).yesNoFormatter() }
                    binding(d.disableWStands, c::disableWStands, c::disableWStands::set)
                    descriptionBuilder {
                        addDefaultText(1)
                    }
                }
                rootOptions.register("wstand_spawn_chance") {
                    controller { IntegerSliderControllerBuilder.create(it).range(0, 100).step(1).formatValue { i -> Component.literal("$i%") } }
                    binding(d.wStandSpawnChance, c::wStandSpawnChance, c::wStandSpawnChance::set)
                }
            }
        }.generateScreen(parent)
    }
}