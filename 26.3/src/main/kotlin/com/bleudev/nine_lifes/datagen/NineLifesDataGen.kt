package com.bleudev.nine_lifes.datagen

import com.bleudev.nine_lifes.datagen.provider.*
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries


class NineLifesDataGen : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        val pack = gen.createPack()
        // Languages
        pack.addProvider(::NLDefaultLanguageProvider)
        pack.addProvider(::NLUpsideDownLanguageProvider)
        pack.addProvider(::NLRussianLanguageProvider)
        // Tags
        pack.addProvider(::NineLifesItemTagsProvider)
        pack.addProvider(::NineLifesDamageTypeTagsProvider)
        // Other
        pack.addProvider(::NineLifesDynamicRegistryProvider)
        pack.addProvider(::NineLifesAdvancementsProvider)
        pack.addProvider(::NineLifesRecipesProvider)
        pack.addProvider(::NineLifesModelProvider)
        pack.addProvider(::NineLifesSoundsProvider)
    }

    override fun buildRegistry(registryBuilder: RegistrySetBuilder) {
        registryBuilder.add(Registries.ENCHANTMENT, NineLifesDynamicRegistryProvider::bootstrapEnchantments)
        registryBuilder.add(Registries.DAMAGE_TYPE, NineLifesDynamicRegistryProvider::bootstrapDamageTypes)
    }
}