package com.bleudev.nine_lifes.custom.entity.ai.goal

import com.bleudev.nine_lifes.custom.entity.WanderingArmorStand
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
import net.minecraft.world.entity.player.Player

class WanderingArmorStandWaterAvoidingRandomStrollGoal(val wanderingArmorStand: WanderingArmorStand) : WaterAvoidingRandomStrollGoal(wanderingArmorStand, 0.3) {
    override fun canUse(): Boolean {
        return wanderingArmorStand.canWander() && super.canUse()
    }
}

class WanderingArmorStandLookAtPlayerGoal(val wanderingArmorStand: WanderingArmorStand) : LookAtPlayerGoal(wanderingArmorStand, Player::class.java, 6f) {
    override fun canUse(): Boolean {
        return wanderingArmorStand.canWander() && super.canUse()
    }
}

class WanderingArmorStandRandomLookAroundGoal(val wanderingArmorStand: WanderingArmorStand) : RandomLookAroundGoal(wanderingArmorStand) {
    override fun canUse(): Boolean {
        return wanderingArmorStand.canWander() && super.canUse()
    }
}