package com.bleudev.nine_lifes.datagen

import com.bleudev.nine_lifes.datagen.provider.*
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries


class NineLifesDataGen : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        val pack = gen.createPack()
        pack.addProvider(::NineLifesDefaultTranslationProvider)
        pack.addProvider(::NineLifesRussianTranslationProvider)
        pack.addProvider(::NineLifesItemTagsProvider)
        pack.addProvider(::NineLifesDynamicRegistryProvider)
        pack.addProvider(::NineLifesDamageTypeTagsProvider)
        pack.addProvider(::NineLifesAdvancementsProvider)
    }

    override fun buildRegistry(registryBuilder: RegistrySetBuilder) {
        registryBuilder.add(Registries.ENCHANTMENT, NineLifesDynamicRegistryProvider::bootstrapEnchantments)
        registryBuilder.add(Registries.DAMAGE_TYPE, NineLifesDynamicRegistryProvider::bootstrapDamageTypes)
    }
}