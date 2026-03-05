package com.bleudev.nine_lifes.custom.advancements.criterion

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.ContextAwarePredicate
import net.minecraft.advancements.criterion.MinMaxBounds
import net.minecraft.advancements.criterion.SimpleCriterionTrigger
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import java.util.*

class CustomStatCriterion : SimpleCriterionTrigger<CustomStatCriterion.TriggerInstance> {
    internal constructor() : super()

    override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

    fun trigger(player: ServerPlayer) = trigger(player) { it.requirementsMet(player.stats.getValue(Stats.CUSTOM, it.stat)) }

    fun require(stat: Identifier, expect: MinMaxBounds.Ints): Criterion<TriggerInstance> = createCriterion(TriggerInstance(Optional.empty(), stat, expect))

    data class TriggerInstance(val playerPredicate: Optional<ContextAwarePredicate>, val stat: Identifier, val expect: MinMaxBounds.Ints): SimpleInstance {
        override fun player(): Optional<ContextAwarePredicate> = playerPredicate

        fun requirementsMet(value: Int): Boolean = expect.matches(value).also {
            println("DEBUG $value -> $expect = $it")
        }

        companion object {
            val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { it.group(
                ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                Identifier.CODEC.fieldOf("stat").forGetter(TriggerInstance::stat),
                MinMaxBounds.Ints.CODEC.fieldOf("expect").forGetter(TriggerInstance::expect)
            ).apply(it, ::TriggerInstance) }
        }
    }
}

