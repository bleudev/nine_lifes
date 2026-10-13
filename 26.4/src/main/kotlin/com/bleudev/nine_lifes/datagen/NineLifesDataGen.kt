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
        pack.addProvider(::NLPreReformRussiandLanguageProvider)
        // Tags
        pack.addProvider(::NLItemTagsProvider)
        pack.addProvider(::NLDamageTypeTagsProvider)
        // Other
        pack.addProvider(::NLDynamicRegistryProvider)
        pack.addProvider(::NLAdvancementsProvider)
        pack.addProvider(::NLRecipesProvider)
        pack.addProvider(::NLModelProvider)
        pack.addProvider(::NLSoundsProvider)
    }

    override fun buildRegistry(registryBuilder: RegistrySetBuilder) {
        registryBuilder.add(Registries.ENCHANTMENT, NLDynamicRegistryProvider::bootstrapEnchantments)
        registryBuilder.add(Registries.DAMAGE_TYPE, NLDynamicRegistryProvider::bootstrapDamageTypes)
    }
}