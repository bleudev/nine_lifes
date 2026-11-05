package com.bleudev.nine_lifes.datagen.provider

import com.bleudev.nine_lifes.custom.NLDamageTypeTags
import com.bleudev.nine_lifes.custom.NLDamageTypes
import com.bleudev.nine_lifes.custom.NLItemTags
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.tags.TagAppender
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import java.util.concurrent.CompletableFuture

class NLItemTagsProvider(output: FabricPackOutput, lookupFuture: CompletableFuture<HolderLookup.Provider>) : FabricTagsProvider.ItemTagsProvider(output, lookupFuture) {
    override fun addTags(registries: HolderLookup.Provider) {
        builder(NLItemTags.CAUSE_BLAST_FURNACE_EXPLODE).add(Items.AMETHYST_SHARD)
        builder(NLItemTags.CAUSE_FURNACE_EXPLODE).add(Items.AMETHYST_SHARD)
        builder(NLItemTags.CAUSE_SMOKER_EXPLODE).add(Items.AMETHYST_SHARD)
        builder(NLItemTags.CAUSE_CAMPFIRE_EXPLODE).add(Items.AMETHYST_SHARD)
        builder(NLItemTags.LIGHTNING_CHARGEABLE).add(Items.AMETHYST_SHARD)

        builder(NLItemTags.Enchantable.CHARGE).add(Items.AMETHYST_SHARD)
        builder(NLItemTags.Enchantable.CHARGE_IN_TABLE)
    }

    @Suppress("DEPRECATION")
    private fun TagAppender<Item>.add(vararg items: Item) {
        for (item in items) {
            this.add(item.builtInRegistryHolder().key())
        }
    }
}

class NLDamageTypeTagsProvider(output: FabricPackOutput, lookupFuture: CompletableFuture<HolderLookup.Provider>) : FabricTagsProvider<DamageType>(output, Registries.DAMAGE_TYPE, lookupFuture ) {
    override fun addTags(registries: HolderLookup.Provider) {
        builder(NLDamageTypeTags.GIVES_LIFE).add(NLDamageTypes.CHARGED_AMETHYST)
        builder(NLDamageTypeTags.CAN_HIT_WSTAND).add(DamageTypes.PLAYER_ATTACK)
        // TODO
//        builder(NineLifesDamageTypeTags.IS_LIGHTNING_OR_FIRE).addTag(DamageTypeTags.IS_LIGHTNING).addTag(DamageTypeTags.IS_FIRE)

        val all = listOf(NLDamageTypes.AMETHYSM, NLDamageTypes.CHARGED_AMETHYST, NLDamageTypes.UNKNOWN)
        builder(DamageTypeTags.BYPASSES_ARMOR).addAll(all)
        builder(DamageTypeTags.BYPASSES_ENCHANTMENTS).addAll(all)
        builder(DamageTypeTags.BYPASSES_INVULNERABILITY).addAll(all)
        builder(DamageTypeTags.BYPASSES_RESISTANCE).addAll(all)
        builder(DamageTypeTags.BYPASSES_SHIELD).addAll(all)
        builder(DamageTypeTags.NO_KNOCKBACK).addAll(all)
    }
}
