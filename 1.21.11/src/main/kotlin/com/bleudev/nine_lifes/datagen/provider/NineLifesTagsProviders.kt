package com.bleudev.nine_lifes.datagen.provider

import com.bleudev.nine_lifes.custom.NineLifesDamageTypeTags
import com.bleudev.nine_lifes.custom.NineLifesDamageTypes
import com.bleudev.nine_lifes.custom.NineLifesItemTags
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.tags.TagAppender
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import java.util.concurrent.CompletableFuture

class NineLifesItemTagsProvider(output: FabricDataOutput, lookupFuture: CompletableFuture<HolderLookup.Provider>) : FabricTagProvider.ItemTagProvider(output, lookupFuture) {
    override fun addTags(registries: HolderLookup.Provider) {
        builder(NineLifesItemTags.CAUSE_BLAST_FURNACE_EXPLODE).add(Items.AMETHYST_SHARD)
        builder(NineLifesItemTags.CAUSE_FURNACE_EXPLODE).add(Items.AMETHYST_SHARD)
        builder(NineLifesItemTags.CAUSE_SMOKER_EXPLODE).add(Items.AMETHYST_SHARD)
        builder(NineLifesItemTags.CAUSE_CAMPFIRE_EXPLODE).add(Items.AMETHYST_SHARD)
        builder(NineLifesItemTags.LIGHTNING_CHARGEABLE).add(Items.AMETHYST_SHARD)

        builder(NineLifesItemTags.Enchantable.CHARGE).add(Items.AMETHYST_SHARD)
        builder(NineLifesItemTags.Enchantable.CHARGE_IN_TABLE)
    }

    @Suppress("DEPRECATION")
    private fun TagAppender<ResourceKey<Item>, Item>.add(vararg items: Item) {
        for (item in items) {
            this.add(item.builtInRegistryHolder().key())
        }
    }
}

class NineLifesDamageTypeTagsProvider(output: FabricDataOutput, lookupFuture: CompletableFuture<HolderLookup.Provider>) : FabricTagProvider<DamageType>(output, Registries.DAMAGE_TYPE, lookupFuture ) {
    override fun addTags(registries: HolderLookup.Provider) {
        builder(NineLifesDamageTypeTags.GIVES_LIFE).add(NineLifesDamageTypes.CHARGED_AMETHYST)
        // TODO
//        builder(NineLifesDamageTypeTags.IS_LIGHTNING_OR_FIRE).addTag(DamageTypeTags.IS_LIGHTNING).addTag(DamageTypeTags.IS_FIRE)

        val all = listOf(NineLifesDamageTypes.AMETHYSM, NineLifesDamageTypes.CHARGED_AMETHYST, NineLifesDamageTypes.UNKNOWN)
        builder(DamageTypeTags.BYPASSES_ARMOR).addAll(all)
        builder(DamageTypeTags.BYPASSES_ENCHANTMENTS).addAll(all)
        builder(DamageTypeTags.BYPASSES_INVULNERABILITY).addAll(all)
        builder(DamageTypeTags.BYPASSES_RESISTANCE).addAll(all)
        builder(DamageTypeTags.BYPASSES_SHIELD).addAll(all)
        builder(DamageTypeTags.NO_KNOCKBACK).addAll(all)
    }
}
