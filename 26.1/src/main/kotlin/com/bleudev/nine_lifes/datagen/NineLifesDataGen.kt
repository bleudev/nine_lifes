package com.bleudev.nine_lifes.datagen

import com.bleudev.nine_lifes.datagen.provider.NineLifesAdvancementsProvider
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

class NineLifesDataGen : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        val pack = gen.createPack()
        pack.addProvider(::NineLifesAdvancementsProvider)
    }
}