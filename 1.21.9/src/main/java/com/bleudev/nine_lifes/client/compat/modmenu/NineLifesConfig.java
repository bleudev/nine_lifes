package com.bleudev.nine_lifes.client.compat.modmenu;

import eu.midnightdust.lib.config.MidnightConfig;

public class NineLifesConfig extends MidnightConfig {
    @Entry
    public static boolean join_message_enabled = true;
    @Entry
    public static boolean heartbeat_enabled = true;

    public enum HeartPosition {
        BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT, TOP_LEFT, TOP_CENTER, TOP_RIGHT
    }
    @Entry
    public static HeartPosition heart_position = HeartPosition.BOTTOM_CENTER;
}
