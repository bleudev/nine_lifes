package com.bleudev.nine_lifes.custom.advancements.criterion

import com.bleudev.nine_lifes.util.currentLifesPlayTime
import com.bleudev.nine_lifes.util.lifes
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.ContextAwarePredicate
import net.minecraft.advancements.criterion.SimpleCriterionTrigger
import net.minecraft.server.level.ServerPlayer
import java.util.*

class LifesPlayTimeCriterion : SimpleCriterionTrigger<LifesPlayTimeCriterion.TriggerInstance> {
    internal constructor() : super()

    override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

    fun trigger(player: ServerPlayer) = trigger(player) { player.isAlive && player.gameMode().isSurvival && it.requirementsMet(player.lifes, player.currentLifesPlayTime) }

    fun require(lifesCount: Int, playTime: Int): Criterion<TriggerInstance> = createCriterion(TriggerInstance(Optional.empty(), lifesCount, playTime))

    data class TriggerInstance(val playerPredicate: Optional<ContextAwarePredicate>, val lifes: Int, val playTime: Int): SimpleInstance {
        override fun player(): Optional<ContextAwarePredicate> = playerPredicate

        fun requirementsMet(lifesCount: Int, lifesPlayTime: Int): Boolean = (lifesCount == lifes) && (lifesPlayTime >= playTime)

        companion object {
            val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { it.group(
                ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                Codec.INT.fieldOf("lifes").forGetter(TriggerInstance::lifes),
                Codec.INT.fieldOf("playTime").forGetter(TriggerInstance::playTime),
            ).apply(it, ::TriggerInstance) }
        }
    }
}

