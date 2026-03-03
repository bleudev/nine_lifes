package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.custom.advancements.criterion.AlmostDeadCriterion
import com.bleudev.nine_lifes.util.createIdentifier
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.CriterionTrigger
import net.minecraft.server.level.ServerPlayer

object NineLifesCriterions {
    val ALMOST_DEAD = create("almost_dead", AlmostDeadCriterion())

    private fun <T : CriterionTrigger<*>> create(name: String, criterion: T) = CriteriaTriggers.register(createIdentifier(name).toString(), criterion)

    internal fun initialize() {}

    internal fun trigger(player: ServerPlayer) {
        ALMOST_DEAD.trigger(player)
    }
}