package com.bleudev.nine_lifes.custom.advancements.criterion

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.predicates.ContextAwarePredicate
import net.minecraft.advancements.triggers.Criterion
import net.minecraft.advancements.triggers.SimpleCriterionTrigger
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.Item
import java.util.*

class ChargeItemCriterion : SimpleCriterionTrigger<ChargeItemCriterion.TriggerInstance> {
    internal constructor() : super()

    override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

    fun trigger(player: ServerPlayer, chargedItem: Item, distance: Double) = trigger(player) { player.isAlive && player.gameMode().isSurvival && it.requirementsMet(chargedItem, distance) }

    fun require(item: Holder<Item>, distance: Double): Criterion<TriggerInstance> = createCriterion(TriggerInstance(Optional.empty(), item, distance))
    fun require(item: Item, distance: Double) = require(item.builtInRegistryHolder(), distance)

    data class TriggerInstance(val playerPredicate: Optional<ContextAwarePredicate>, val item: Holder<Item>, val distance: Double): SimpleInstance {
        override fun player(): Optional<ContextAwarePredicate> = playerPredicate

        fun requirementsMet(chargedItem: Item, distance: Double): Boolean =
            (item.value() == chargedItem) && (distance <= this.distance)

        companion object {
            val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { it.group(
                ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                Item.CODEC.fieldOf("item").forGetter(TriggerInstance::item),
                Codec.DOUBLE.fieldOf("distance").forGetter(TriggerInstance::distance),
            ).apply(it, ::TriggerInstance) }
        }
    }
}

