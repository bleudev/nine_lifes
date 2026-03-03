package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.custom.advancements.criterion.*
import com.bleudev.nine_lifes.util.createIdentifier
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.CriterionTrigger
import net.minecraft.server.level.ServerPlayer

object NineLifesCriterions {
    val BED_SLEEPING_PROBLEM = create("bed_sleeping_problem", BedSleepingProblemCriterion())
    val SUCCESS_SLEEP_WITH_AMETHYSM = create("success_sleep_with_amethysm", SuccessSleepWithAmethysmCriterion())
    val ALMOST_DEAD = create("almost_dead", AlmostDeadCriterion())
    val LIFES_PLAY_TIME = create("lifes_play_time", LifesPlayTimeCriterion())
    val PLAY_TIME = create("play_time", PlayTimeCriterion())
    val CHARGE_ITEM = create("charge_item", ChargeItemCriterion())

    private fun <T : CriterionTrigger<*>> create(name: String, criterion: T) = CriteriaTriggers.register(createIdentifier(name).toString(), criterion)

    internal fun initialize() {}

    internal fun trigger(player: ServerPlayer) {
        ALMOST_DEAD.trigger(player)
        LIFES_PLAY_TIME.trigger(player)
        PLAY_TIME.trigger(player)
    }
}