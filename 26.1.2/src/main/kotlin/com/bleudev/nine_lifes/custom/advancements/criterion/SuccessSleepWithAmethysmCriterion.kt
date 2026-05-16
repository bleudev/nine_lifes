package com.bleudev.nine_lifes.custom.advancements.criterion

import com.bleudev.nine_lifes.util.lifes
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.ContextAwarePredicate
import net.minecraft.advancements.criterion.SimpleCriterionTrigger
import net.minecraft.server.level.ServerPlayer
import java.util.*

class SuccessSleepWithAmethysmCriterion : SimpleCriterionTrigger<SuccessSleepWithAmethysmCriterion.TriggerInstance> {
    internal constructor() : super()

    override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

    fun trigger(player: ServerPlayer) = trigger(player) { player.isAlive && player.gameMode().isSurvival && it.requirementsMet(player.lifes <= 5) }

    fun require(wasRequired: Boolean): Criterion<TriggerInstance> = createCriterion(TriggerInstance(Optional.empty(), wasRequired))

    data class TriggerInstance(val playerPredicate: Optional<ContextAwarePredicate>, val wasRequired: Boolean): SimpleInstance {
        override fun player(): Optional<ContextAwarePredicate> = playerPredicate

        fun requirementsMet(wasRequired: Boolean): Boolean = (this.wasRequired && wasRequired) || (!this.wasRequired)

        companion object {
            val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { it.group(
                ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                Codec.BOOL.fieldOf("wasRequired").forGetter(TriggerInstance::wasRequired),
            ).apply(it, ::TriggerInstance) }
        }
    }
}

