package com.bleudev.nine_lifes.client.compat.modmenu;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "nine_lifes")
public class ClothAutoConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public boolean join_message_enabled = true;
}
