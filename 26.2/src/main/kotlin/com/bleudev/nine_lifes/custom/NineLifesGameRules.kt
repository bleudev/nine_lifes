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
    fun initialize() {}
}