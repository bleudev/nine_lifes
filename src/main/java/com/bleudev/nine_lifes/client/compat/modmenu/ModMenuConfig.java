package com.bleudev.nine_lifes.client.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ModMenuConfig implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(ClothConfig.class, parent).get();
    }

    @Config(name = "nine_lifes")
    public static class ClothConfig implements ConfigData {
        @ConfigEntry.Gui.Tooltip
        public boolean join_message_enabled = true;
    }
}
