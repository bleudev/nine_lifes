package com.bleudev.nine_lifes.datagen

import com.bleudev.nine_lifes.datagen.provider.DefaultTranslationProvider
import com.bleudev.nine_lifes.datagen.provider.NineLifesAdvancementsProvider
import com.bleudev.nine_lifes.datagen.provider.NineLifesDynamicRegistryProvider
import com.bleudev.nine_lifes.datagen.provider.RussianTranslationProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries


class NineLifesDataGen : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        val pack = gen.createPack()
        pack.addProvider(::DefaultTranslationProvider)
        pack.addProvider(::RussianTranslationProvider)
        pack.addProvider(::NineLifesDynamicRegistryProvider)
        pack.addProvider(::NineLifesAdvancementsProvider)
    }

    override fun buildRegistry(registryBuilder: RegistrySetBuilder) {
        registryBuilder.add(Registries.ENCHANTMENT, NineLifesDynamicRegistryProvider::bootstrapEnchantments)
    }
}