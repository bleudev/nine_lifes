package com.bleudev.nine_lifes.custom.advancements.criterion

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.ContextAwarePredicate
import net.minecraft.advancements.criterion.SimpleCriterionTrigger
import net.minecraft.network.chat.contents.TranslatableContents
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import java.util.*

class BedSleepingProblemCriterion : SimpleCriterionTrigger<BedSleepingProblemCriterion.TriggerInstance> {
    internal constructor() : super()

    override fun codec(): Codec<TriggerInstance> = TriggerInstance.CODEC

    fun trigger(player: ServerPlayer, problem: Player.BedSleepingProblem) = trigger(player) { player.isAlive && player.gameMode().isSurvival && it.requirementsMet(problem) }

    fun require(vararg problems: Player.BedSleepingProblem): Criterion<TriggerInstance> = createCriterion(
        TriggerInstance(Optional.empty(), problems.mapNotNull{(it.message?.contents as? TranslatableContents)?.key}))

    data class TriggerInstance(val playerPredicate: Optional<ContextAwarePredicate>, val problems: List<String>): SimpleInstance {
        override fun player(): Optional<ContextAwarePredicate> = playerPredicate

        fun requirementsMet(problem: Player.BedSleepingProblem): Boolean = (problem.message?.contents as? TranslatableContents)?.key in problems

        companion object {
            val CODEC: Codec<TriggerInstance> = RecordCodecBuilder.create { it.group(
                ContextAwarePredicate.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                Codec.list(Codec.STRING).fieldOf("problems").forGetter(TriggerInstance::problems)
            ).apply(it, ::TriggerInstance) }
        }
    }
}

