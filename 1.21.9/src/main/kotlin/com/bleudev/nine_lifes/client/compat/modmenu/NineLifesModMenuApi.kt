package com.bleudev.nine_lifes.client.compat.modmenu

import com.bleudev.nine_lifes.MOD_ID
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import eu.midnightdust.lib.config.MidnightConfig
import net.minecraft.client.gui.screens.Screen

class NineLifesModMenuApi : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> =
        { p: Screen -> MidnightConfig.getScreen(p, MOD_ID) } as ConfigScreenFactory<*>
}