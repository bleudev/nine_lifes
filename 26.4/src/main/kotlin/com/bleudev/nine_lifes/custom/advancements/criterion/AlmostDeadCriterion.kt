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

class AlmostDeadCriterion : SimpleCriterionTrigger<AlmostDeadCriterion.TriggerInstance> {
    internal constructor() : super()

    override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

    fun trigger(player: ServerPlayer) = trigger(player) { player.isAlive && player.gameMode().isSurvival && it.requirementsMet(player.lifes, player.health) }

    fun require(lifes: Int, health: Float): Criterion<TriggerInstance> = createCriterion(TriggerInstance(Optional.empty(), lifes, health))

    data class TriggerInstance(val playerPredicate: Optional<Holder<LootItemCondition>>, val lifes: Int, val maxHealth: Float): SimpleInstance {
        override fun player(): Optional<Holder<LootItemCondition>> = playerPredicate

        fun requirementsMet(lifes: Int, health: Float): Boolean = (lifes == this@TriggerInstance.lifes) && (health <= maxHealth)

        companion object {
            val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { it.group(
                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                Codec.INT.fieldOf("lifes").forGetter(TriggerInstance::lifes),
                Codec.FLOAT.fieldOf("maxHealth").forGetter(TriggerInstance::maxHealth),
            ).apply(it, ::TriggerInstance) }
        }
    }
}