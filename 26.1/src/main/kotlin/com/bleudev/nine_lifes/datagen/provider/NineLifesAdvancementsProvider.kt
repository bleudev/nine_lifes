package com.bleudev.nine_lifes.datagen.provider

import com.bleudev.nine_lifes.MOD_ID
import com.bleudev.nine_lifes.custom.NineLifesCriterions
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.criterion.ConsumeItemTrigger
import net.minecraft.advancements.criterion.PlayerTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
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

        val root = consumer.create("root", false, Items.AMETHYST_SHARD, AdvancementType.TASK) {
            addCriterion("joined", PlayerTrigger.TriggerInstance.tick())
        }
        val ateAmethystShard = consumer.create("ate_amethyst_shard", false, Items.AMETHYST_SHARD, AdvancementType.TASK) {
            addCriterion("ate_amethyst_shard", ConsumeItemTrigger.TriggerInstance.usedItem(itemLookup, Items.AMETHYST_SHARD))
            parent(root)
        }
        val almostDead = consumer.create("almost_dead", true, Items.IRON_SWORD, AdvancementType.CHALLENGE) {
            addCriterion("almost_dead", NineLifesCriterions.ALMOST_DEAD.require(1, 1f))
            rewards(AdvancementRewards.Builder.experience(10))
            parent(root)
        }
    }

    private val background: Identifier = Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure")

    private var isRoot: Boolean = true
    private fun Consumer<AdvancementHolder>.create(name: String, hidden: Boolean, icon: Item, type: AdvancementType, applier: Advancement.Builder.() -> Advancement.Builder): AdvancementHolder = Advancement.Builder.advancement().display(
        icon, Component.translatable("advancement.nine_lifes.$name"), Component.translatable("advancement.nine_lifes.description.$name"), if (isRoot) background else null, type, name != "root", name != "root", hidden)
        .applier().save(this, "$MOD_ID:$name").also { isRoot = false }
}