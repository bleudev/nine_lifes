package com.bleudev.nine_lifes.custom.advancements.criterion

import com.bleudev.nine_lifes.util.playTime
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.triggers.Criterion
import net.minecraft.advancements.triggers.SimpleCriterionTrigger
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import java.util.*

class PlayTimeCriterion : SimpleCriterionTrigger<PlayTimeCriterion.TriggerInstance> {
    internal constructor() : super()

    override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

    fun trigger(player: ServerPlayer) = trigger(player) { player.isAlive && player.gameMode().isSurvival && it.requirementsMet(player.playTime) }

    fun require(playTime: Int): Criterion<TriggerInstance> = createCriterion(TriggerInstance(Optional.empty(), playTime))

    data class TriggerInstance(val playerPredicate: Optional<Holder<LootItemCondition>>, val playTime: Int): SimpleInstance {
        override fun player(): Optional<Holder<LootItemCondition>> = playerPredicate

        fun requirementsMet(playTime: Int): Boolean = playTime >= this.playTime

        companion object {
            val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { it.group(
                LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                Codec.INT.fieldOf("playtime").forGetter(TriggerInstance::playTime)
            ).apply(it, ::TriggerInstance) }
        }
    }
}

