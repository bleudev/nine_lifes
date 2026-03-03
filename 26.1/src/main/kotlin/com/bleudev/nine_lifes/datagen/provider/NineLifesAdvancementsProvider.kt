package com.bleudev.nine_lifes.datagen.provider

import com.bleudev.nine_lifes.MOD_ID
import com.bleudev.nine_lifes.PROBLEM_NOT_NOW
import com.bleudev.nine_lifes.custom.NineLifesCriterions
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.criterion.PlayerTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer


class NineLifesAdvancementsProvider(output: FabricPackOutput,
                                    registryLookup: CompletableFuture<HolderLookup.Provider>
) : FabricAdvancementProvider(output, registryLookup) {
    override fun generateAdvancement(
        registryLookup: HolderLookup.Provider,
        consumer: Consumer<AdvancementHolder>
    ) {
        val root = consumer.create("root", false, Items.AMETHYST_SHARD, AdvancementType.TASK) {
            addCriterion("joined", PlayerTrigger.TriggerInstance.tick())
        }
        val trySleepWithoutShard = consumer.create("try_sleep_without_shard", true, Items.RED_BED, AdvancementType.TASK) {
            addCriterion("tried_sleep", NineLifesCriterions.BED_SLEEPING_PROBLEM.require(Player.BedSleepingProblem.NOT_SAFE, PROBLEM_NOT_NOW))
            parent(root)
        }
        val sleptWithShard = consumer.create("slept_with_shard", true, Items.BLUE_BED, AdvancementType.TASK) {
            addCriterion("slept_with_shard", NineLifesCriterions.SUCCESS_SLEEP_WITH_AMETHYSM.require(true))
            parent(trySleepWithoutShard)
        }
        val almostDead = consumer.create("almost_dead", true, Items.IRON_SWORD, AdvancementType.CHALLENGE) {
            addCriterion("almost_dead", NineLifesCriterions.ALMOST_DEAD.require(1, 1f))
            rewards(AdvancementRewards.Builder.experience(10))
            parent(root)
        }
    }

    private val background: Identifier = Identifier.withDefaultNamespace("block/amethyst_block")

    private var isRoot: Boolean = true
    private fun Consumer<AdvancementHolder>.create(name: String, hidden: Boolean, icon: Item, type: AdvancementType, applier: Advancement.Builder.() -> Advancement.Builder): AdvancementHolder = Advancement.Builder.advancement().display(
        icon, Component.translatable("advancement.nine_lifes.$name"), Component.translatable("advancement.nine_lifes.description.$name"), if (isRoot) background else null, type, name != "root", name != "root", hidden)
        .applier().save(this, "$MOD_ID:$name").also { isRoot = false }
}