package com.bleudev.nine_lifes.custom;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class CustomGameRules {
    public static final GameRules.Key<GameRules.BooleanRule> NINE_LIFES_MODE_ENABLED = GameRuleRegistry
        .register("nineLifesModeEnabled", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));

    public static void initialize() {}
}
