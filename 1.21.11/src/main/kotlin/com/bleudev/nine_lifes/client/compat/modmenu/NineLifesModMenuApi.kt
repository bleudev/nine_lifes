package com.bleudev.nine_lifes.client.compat.modmenu

import com.bleudev.nine_lifes.MOD_ID
import com.bleudev.nine_lifes.client.config.*
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder
import dev.isxander.yacl3.api.controller.EnumControllerBuilder
import dev.isxander.yacl3.dsl.OptionDsl
import dev.isxander.yacl3.dsl.YetAnotherConfigLib
import net.minecraft.client.gui.screens.Screen
import kotlin.reflect.KMutableProperty

class NineLifesModMenuApi : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> = { parent: Screen ->
        YetAnotherConfigLib(MOD_ID) {
            categories.register("general") {
                rootOptions.register("join_message") {
                    binding(true, ::joinMessageEnabled)
                    yesNoFormat()
                }
                rootOptions.register("heartbeat") {
                    binding(true, ::heartbeatEnabled)
                    yesNoFormat()
                }
                rootOptions.register("heart_position") {
                    binding(HeartPosition.BOTTOM_CENTER, ::heartPosition)
                    enumFormat()
                }
                rootOptions.register("low_lifes_red_sky") {
                    binding(true, ::lowLifesRedSkyEnabled)
                    yesNoFormat()
                }
            }
        }.generateScreen(parent)
    }
}

private fun OptionDsl<Boolean>.yesNoFormat() = controller { BooleanControllerBuilder.create(it).yesNoFormatter() }
private inline fun <reified T : Enum<T>> OptionDsl<T>.enumFormat() = controller { EnumControllerBuilder.create(it).enumClass(T::class.java) }
private fun <T : Any> OptionDsl<T>.binding(default: T, property: KMutableProperty<T>) = binding(default, {property.getter.call()}, {property.setter.call(it)})