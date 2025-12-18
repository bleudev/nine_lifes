package com.bleudev.nine_lifes.client.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class NineLifesConfig extends MidnightConfig {
    @Entry
    public static boolean join_message_enabled = true;

    @Entry
    public static boolean heartbeat_enabled = true;

    @Entry
    public static HeartPosition heart_position = HeartPosition.BOTTOM_CENTER;

    public enum HeartPosition {
        BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT, TOP_LEFT, TOP_CENTER, TOP_RIGHT
    }
}
