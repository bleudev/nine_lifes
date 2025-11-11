package com.bleudev.nine_lifes;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static com.bleudev.nine_lifes.PlatformHelper.*;
import static com.bleudev.nine_lifes.compat.VersionCompat.getPosCompat;

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

    @Contract(value = "_ -> new", pure = true)
    protected static @NotNull Box windChargeActionBox(@NotNull Vec3d pos) {
        return Box.of(pos, 3, 3, 3);
    }
    @Contract("_ -> new")
    protected static @NotNull Box windChargeActionBox(@NotNull Entity wind_charge) {
        return windChargeActionBox(getPosCompat(wind_charge));
    }
}
