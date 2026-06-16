package com.bleudev.nine_lifes.client.compat.modmenu

import com.bleudev.nine_lifes.client.config.generateGuiConfigScreen
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi

class NineLifesModMenuApi : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> = { generateGuiConfigScreen(it) }
}