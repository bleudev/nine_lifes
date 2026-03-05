package com.bleudev.nine_lifes.custom.advancements.criterion

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.ContextAwarePredicate
import net.minecraft.advancements.criterion.SimpleCriterionTrigger
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerPlayer
import java.util.*

class AdvancementCriterion : SimpleCriterionTrigger<AdvancementCriterion.TriggerInstance> {
    internal constructor() : super()

    override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

    fun trigger(player: ServerPlayer) = trigger(player) { player.advancements.getOrStartProgress(it.holder).isDone }

    fun require(advancement: AdvancementHolder): Criterion<TriggerInstance> = createCriterion(TriggerInstance(Optional.empty(), advancement))

    data class TriggerInstance(val playerPredicate: Optional<ContextAwarePredicate>, val holder: AdvancementHolder): SimpleInstance {
        override fun player(): Optional<ContextAwarePredicate> = playerPredicate

        companion object {
            val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { it.group(
                ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                RecordCodecBuilder.create { it2 -> it2.group(
                    Identifier.CODEC.fieldOf("id").forGetter(AdvancementHolder::id),
                    Advancement.CODEC.fieldOf("value").forGetter(AdvancementHolder::value)
                ).apply(it2, ::AdvancementHolder) }.fieldOf("holder").forGetter(TriggerInstance::holder)
            ).apply(it, ::TriggerInstance) }
        }
    }
}
