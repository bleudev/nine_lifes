package com.bleudev.nine_lifes.custom;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

public class CustomGameRules {
    public static final GameRules.Key<GameRules.BooleanRule> DO_LIFES_DECREASE_IN_OVERWORLD;
    public static final GameRules.Key<GameRules.BooleanRule> DO_LIFES_DECREASE_IN_THE_NETHER;
    public static final GameRules.Key<GameRules.BooleanRule> DO_LIFES_DECREASE_IN_THE_END;

    static {
        DO_LIFES_DECREASE_IN_OVERWORLD = GameRuleRegistry.register("doLifesDecreaseInOverworld", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
        DO_LIFES_DECREASE_IN_THE_NETHER = GameRuleRegistry.register("doLifesDecreaseInTheNether", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
        DO_LIFES_DECREASE_IN_THE_END = GameRuleRegistry.register("doLifesDecreaseInTheEnd", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));
    }

    public static void initialize() {}
}
