package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.util.createIdentifier
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder
import net.minecraft.world.level.gamerules.GameRule
import net.minecraft.world.level.gamerules.GameRuleCategory


object NineLifesGameRules {
    val CATEGORY: GameRuleCategory = GameRuleCategory
        .register(createIdentifier("general"))

    val TAKE_LIFES: GameRule<Boolean> = GameRuleBuilder
        .forBoolean(true)
        .category(CATEGORY)
        .buildAndRegister(createIdentifier("take_lifes"))
    val TAKE_LIFES_IN_OVERWORLD: GameRule<Boolean> = GameRuleBuilder
        .forBoolean(true)
        .category(CATEGORY)
        .buildAndRegister(createIdentifier("take_lifes_in_overworld"))
    val TAKE_LIFES_IN_NETHER: GameRule<Boolean> = GameRuleBuilder
        .forBoolean(true)
        .category(CATEGORY)
        .buildAndRegister(createIdentifier("take_lifes_in_nether"))
    val TAKE_LIFES_IN_END: GameRule<Boolean> = GameRuleBuilder
        .forBoolean(true)
        .category(CATEGORY)
        .buildAndRegister(createIdentifier("take_lifes_in_end"))
    val MAX_CHARGED_ITEMS_AT_A_TIME: GameRule<Int> = GameRuleBuilder
        .forInteger(5)
        .range(-1, Int.MAX_VALUE)
        .category(CATEGORY)
        .buildAndRegister(createIdentifier("max_charged_items_at_a_time"))
    fun initialize() {}
}