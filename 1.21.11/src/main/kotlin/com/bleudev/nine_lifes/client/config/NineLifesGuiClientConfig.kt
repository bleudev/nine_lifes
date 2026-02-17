package com.bleudev.nine_lifes.client.config

import com.bleudev.nine_lifes.MOD_ID
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder
import dev.isxander.yacl3.api.controller.EnumControllerBuilder
import dev.isxander.yacl3.dsl.OptionDsl
import dev.isxander.yacl3.dsl.YetAnotherConfigLib
import dev.isxander.yacl3.dsl.descriptionBuilder
import net.minecraft.client.gui.screens.Screen
import kotlin.reflect.KMutableProperty

fun generateGuiConfigScreen(parent: Screen?): Screen = YetAnotherConfigLib(MOD_ID) {
    categories.register("general") {
        rootOptions.register("join_message") {
            binding(true, ::joinMessageEnabled)
            yesNoFormat()
            descriptionBuilder {
                addDefaultText(1)
            }
        }
        rootOptions.register("heartbeat") {
            binding(true, ::heartbeatEnabled)
            yesNoFormat()
            descriptionBuilder {
                addDefaultText(1)
            }
        }
        rootOptions.register("heart_position") {
            binding(HeartPosition.BOTTOM_CENTER, ::heartPosition)
            enumFormat()
            descriptionBuilder {
                addDefaultText(1)
            }
        }
        rootOptions.register("low_lifes_red_sky") {
            binding(true, ::lowLifesRedSkyEnabled)
            yesNoFormat()
            descriptionBuilder {
                addDefaultText(1)
            }
        }
    }
}.generateScreen(parent)

private fun OptionDsl<Boolean>.yesNoFormat() = controller { BooleanControllerBuilder.create(it).yesNoFormatter().coloured(true) }
private inline fun <reified T : Enum<T>> OptionDsl<T>.enumFormat() = controller { EnumControllerBuilder.create(it).enumClass(T::class.java) }
private fun <T : Any> OptionDsl<T>.binding(default: T, property: KMutableProperty<T>) = binding(default, {property.getter.call()}, {property.setter.call(it)})