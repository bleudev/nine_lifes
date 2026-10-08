package com.bleudev.nine_lifes.custom.advancements.criterion

import com.bleudev.nine_lifes.NineLifesStats
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.triggers.Criterion
import net.minecraft.advancements.triggers.SimpleCriterionTrigger
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import java.util.*

class UsedChargedTotalCriterion : SimpleCriterionTrigger<UsedChargedTotalCriterion.TriggerInstance> {
    internal constructor() : super()

    override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

    fun trigger(player: ServerPlayer) = trigger(player) { it.requirementsMet(player.stats.getValue(Stats.CUSTOM, NineLifesStats.USED_CHARGED)) }

    fun require(total: Int): Criterion<TriggerInstance> = createCriterion(TriggerInstance(Optional.empty(), total))

    data class TriggerInstance(val playerPredicate: Optional<Holder<LootItemCondition>>, val total: Int): SimpleInstance {
        override fun player(): Optional<Holder<LootItemCondition>> = playerPredicate

        fun requirementsMet(total: Int): Boolean = this.total <= total

        companion object {
            val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { it.group(
                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                Codec.INT.fieldOf("total").forGetter(TriggerInstance::total),
            ).apply(it, ::TriggerInstance) }
        }
    }
}

