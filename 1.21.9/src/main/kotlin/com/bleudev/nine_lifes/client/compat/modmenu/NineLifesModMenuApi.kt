package com.bleudev.nine_lifes.client.compat.modmenu

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import eu.midnightdust.lib.config.MidnightConfig
import net.minecraft.client.gui.screens.Screen

class NineLifesModMenuApi : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return { parent: Screen -> MidnightConfig.getScreen(parent, "nine_lifes") } as ConfigScreenFactory<*>
    }
}