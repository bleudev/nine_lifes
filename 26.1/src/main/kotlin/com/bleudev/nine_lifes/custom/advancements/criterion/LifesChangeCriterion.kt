package com.bleudev.nine_lifes.custom.advancements.criterion

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.ContextAwarePredicate
import net.minecraft.advancements.criterion.SimpleCriterionTrigger
import net.minecraft.server.level.ServerPlayer
import java.util.*
import kotlin.math.abs

class LifesChangeCriterion : SimpleCriterionTrigger<LifesChangeCriterion.TriggerInstance> {
    internal constructor() : super()

    override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

    fun trigger(player: ServerPlayer, delta: Int, charged: Boolean) = trigger(player) { it.requirementsMet(delta, charged) }

    fun require(minDelta: Int, charged: Boolean): Criterion<TriggerInstance> = createCriterion(TriggerInstance(Optional.empty(), minDelta, charged))

    data class TriggerInstance(val playerPredicate: Optional<ContextAwarePredicate>, val minDelta: Int, val charged: Boolean): SimpleInstance {
        override fun player(): Optional<ContextAwarePredicate> = playerPredicate

        fun requirementsMet(delta: Int, charged: Boolean): Boolean = (charged == this.charged) && (abs(delta) >= abs(minDelta)) && ((delta >= 0 && minDelta >= 0) || (delta <= 0 && minDelta <= 0))

        companion object {
            val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { it.group(
                ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                Codec.INT.fieldOf("minDelta").forGetter(TriggerInstance::minDelta),
                Codec.BOOL.fieldOf("charged").forGetter(TriggerInstance::charged),
            ).apply(it, ::TriggerInstance) }
        }
    }
}

