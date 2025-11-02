package com.bleudev.nine_lifes;

import static com.bleudev.nine_lifes.PlatformHelper.*;

public class NineLifesConst {
    public static final String MOD_ID = "nine_lifes";
    public static final String NAME = getModName(MOD_ID);
    public static final String AUTHOR = getModAuthors(MOD_ID).stream().findFirst().orElse("Unknown");
    public static final String VERSION = getModVersion(MOD_ID, "+");
    public static final String GITHUB_LINK = "https://github.com/bleudev/nine_lifes";
    public static final String ISSUES_LINK = "https://github.com/bleudev/nine_lifes/issues";
    public static final String MODRINTH_LINK = "https://modrinth.com/mod/nine_lifes";

    public static final float WANDERING_ARMOR_STAND_SPAWN_CHANCE = 0.5f;

    public static final int LIGHTNING_CHARGING_RADIUS = 1;
    public static final int CHARGE_SCREEN_EFFECT_RADIUS_MIN = 3;
    public static final int CHARGE_SCREEN_EFFECT_RADIUS_MAX = 20;
    public static final double CHARGE_SCREEN_MAX_STRENGTH = 0.5;
    public static final int CHARGE_SCREEN_DURATION = 6;
}
