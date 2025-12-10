package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.util.createResourceLocation
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.item.Item

private fun <T> ofRegistry(registry: ResourceKey<Registry<T>>, name: String): TagKey<T> =
    TagKey.create(registry, createResourceLocation(name))

object NineLifesItemTags {
    @JvmField
    val CAUSE_BLAST_FURNACE_EXPLODE: TagKey<Item> = of("cause_blast_furnace_explode")
    @JvmField
    val CAUSE_FURNACE_EXPLODE: TagKey<Item> = of("cause_furnace_explode")
    @JvmField
    val CAUSE_SMOKER_EXPLODE: TagKey<Item> = of("cause_smoker_explode")
    @JvmField
    val CAUSE_CAMPFIRE_EXPLODE: TagKey<Item> = of("cause_campfire_explode")
    val LIGHTNING_CHARGEABLE: TagKey<Item> = of("lightning_chargeable")

    private fun of(name: String): TagKey<Item> = ofRegistry(Registries.ITEM, name)
}

object NineLifesDamageTypeTags {
    val IS_LIGHTNING_OR_FIRE: TagKey<DamageType> = of("is_lightning_or_fire")

    private fun of(name: String): TagKey<DamageType> = ofRegistry(Registries.DAMAGE_TYPE, name)
}
