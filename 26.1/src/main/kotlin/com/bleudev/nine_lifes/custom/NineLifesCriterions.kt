package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.custom.advancements.criterion.AlmostDeadCriterion
import com.bleudev.nine_lifes.custom.advancements.criterion.BedSleepingProblemCriterion
import com.bleudev.nine_lifes.custom.advancements.criterion.SuccessSleepWithAmethysmCriterion
import com.bleudev.nine_lifes.util.createIdentifier
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.CriterionTrigger
import net.minecraft.server.level.ServerPlayer

object NineLifesCriterions {
    val BED_SLEEPING_PROBLEM = create("bed_sleeping_problem", BedSleepingProblemCriterion())
    val SUCCESS_SLEEP_WITH_AMETHYSM = create("success_sleep_with_amethysm", SuccessSleepWithAmethysmCriterion())
    val ALMOST_DEAD = create("almost_dead", AlmostDeadCriterion())

    private fun <T : CriterionTrigger<*>> create(name: String, criterion: T) = CriteriaTriggers.register(createIdentifier(name).toString(), criterion)

    internal fun initialize() {}

    internal fun trigger(player: ServerPlayer) {
        ALMOST_DEAD.trigger(player)
    }
}