package com.bleudev.nine_lifes.datagen.provider

import com.bleudev.nine_lifes.CHARGE_SCREEN_EFFECT_RADIUS_MAX
import com.bleudev.nine_lifes.MOD_ID
import com.bleudev.nine_lifes.PROBLEM_NOT_NOW
import com.bleudev.nine_lifes.custom.NineLifesCriterions
import com.bleudev.nine_lifes.custom.NineLifesEnchantments
import com.bleudev.nine_lifes.util.advancement
import com.bleudev.nine_lifes.util.advancementDescription
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.criterion.*
import net.minecraft.advancements.criterion.MinMaxBounds.Ints
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.predicates.DataComponentPredicates
import net.minecraft.core.component.predicates.EnchantmentsPredicate
import net.minecraft.core.registries.Registries
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
        val itemLookup = registryLookup.lookupOrThrow(Registries.ITEM)
        val enchantments = registryLookup.lookupOrThrow(Registries.ENCHANTMENT)

        val chargedAmethystShardPredicate = ItemPredicate.Builder.item()
            .of(itemLookup, Items.AMETHYST_SHARD)
            .withComponents(DataComponentMatchers.Builder.components()
                .partial(DataComponentPredicates.ENCHANTMENTS, EnchantmentsPredicate.enchantments(listOf(
                    EnchantmentPredicate(NineLifesEnchantments.Holders.charge(enchantments), Ints.atLeast(1)))))
                .build())

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
        val gotChargedShard = consumer.create("got_charged_shard", true, Items.AMETHYST_SHARD, AdvancementType.GOAL) {
            requirements(AdvancementRequirements.Strategy.OR)
            addCriterion("got_charged_shard_with_lightning", NineLifesCriterions.CHARGE_ITEM.require(Items.AMETHYST_SHARD, CHARGE_SCREEN_EFFECT_RADIUS_MAX.toDouble()))
            addCriterion("got_charged_shard", InventoryChangeTrigger.TriggerInstance.hasItems(chargedAmethystShardPredicate))
            parent(trySleepWithoutShard)
        }
        val gotLifeWithShard = consumer.create("got_life_with_shard", true, Items.AMETHYST_SHARD, AdvancementType.TASK) {
            addCriterion("got_life_with_shard", NineLifesCriterions.LIFES_CHANGE.require(1, true))
            parent(gotChargedShard)
        }
        val ate64ChargedShards = consumer.create("ate_64_charged_shards", false, Items.SKELETON_SKULL, AdvancementType.CHALLENGE) {
            for (i in 1..64) {
                addCriterion("ate_${i}_charged_shards", NineLifesCriterions.USED_CHARGED_TOTAL.require(i))
            }

            parent(gotLifeWithShard)
        }
        val almostDead = consumer.create("almost_dead", true, Items.IRON_SWORD, AdvancementType.CHALLENGE) {
            addCriterion("almost_dead", NineLifesCriterions.ALMOST_DEAD.require(1, 1f))
            parent(root)
        }
        val hundredDays = consumer.create("hundred_days", false, Items.SKELETON_SKULL, AdvancementType.GOAL) {
            addCriterion("hundred_days_played", NineLifesCriterions.PLAY_TIME.require(2400000))
            parent(root)
        }
        val trueHundredDays = consumer.create("true_hundred_days", true, Items.WITHER_SKELETON_SKULL, AdvancementType.CHALLENGE) {
            addCriterion("hundred_days_played_with_one_life", NineLifesCriterions.LIFES_PLAY_TIME.require(1, 2400000))
            parent(hundredDays)
        }

        consumer.create("all_done", false, Items.DIAMOND, AdvancementType.CHALLENGE) {
            requireAdvancement(trySleepWithoutShard)
            requireAdvancement(sleptWithShard)
            requireAdvancement(gotChargedShard)
            requireAdvancement(gotLifeWithShard)
            requireAdvancement(ate64ChargedShards)
            requireAdvancement(almostDead)
            requireAdvancement(hundredDays)
            requireAdvancement(trueHundredDays)

            parent(root)
        }
    }

    private val background: Identifier = Identifier.withDefaultNamespace("block/amethyst_block")

    private var isRoot: Boolean = true
    private fun Consumer<AdvancementHolder>.create(name: String, hidden: Boolean, icon: Item, type: AdvancementType, applier: Advancement.Builder.() -> Advancement.Builder): AdvancementHolder = Advancement.Builder.advancement().display(
        icon, Component.translatable(advancement(name)), Component.translatable(advancementDescription(name)), if (isRoot) background else null, type, name != "root", name != "root", hidden)
        .applier().save(this, "$MOD_ID:$name").also { isRoot = false }

    private fun Advancement.Builder.requireAdvancement(holder: AdvancementHolder) =
        addCriterion("require_${holder.id.namespace}_${holder.id.path}", NineLifesCriterions.ADVANCEMENT.require(holder))
}