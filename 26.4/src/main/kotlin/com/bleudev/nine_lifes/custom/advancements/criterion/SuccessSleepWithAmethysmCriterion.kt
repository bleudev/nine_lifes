package com.bleudev.nine_lifes.custom.advancements.criterion

import com.bleudev.nine_lifes.util.lifes
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.triggers.Criterion
import net.minecraft.advancements.triggers.SimpleCriterionTrigger
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import java.util.*

class SuccessSleepWithAmethysmCriterion : SimpleCriterionTrigger<SuccessSleepWithAmethysmCriterion.TriggerInstance> {
    internal constructor() : super()

    override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

    fun trigger(player: ServerPlayer) = trigger(player) { player.isAlive && player.gameMode().isSurvival && it.requirementsMet(player.lifes <= 5) }

    fun require(wasRequired: Boolean): Criterion<TriggerInstance> = createCriterion(TriggerInstance(Optional.empty(), wasRequired))

    data class TriggerInstance(val playerPredicate: Optional<Holder<LootItemCondition>>, val wasRequired: Boolean): SimpleInstance {
        override fun player(): Optional<Holder<LootItemCondition>> = playerPredicate

        fun requirementsMet(wasRequired: Boolean): Boolean = (this.wasRequired && wasRequired) || (!this.wasRequired)

        companion object {
            val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { it.group(
                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                Codec.BOOL.fieldOf("wasRequired").forGetter(TriggerInstance::wasRequired),
            ).apply(it, ::TriggerInstance) }
        }
    }
}

