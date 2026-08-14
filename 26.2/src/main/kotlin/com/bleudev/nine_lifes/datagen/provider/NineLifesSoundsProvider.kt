package com.bleudev.nine_lifes.datagen.provider

import com.bleudev.nine_lifes.custom.NineLifesSounds
import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class NineLifesSoundsProvider(output: PackOutput, registriesFuture: CompletableFuture<HolderLookup.Provider>) : FabricSoundsProvider(output, registriesFuture) {
    override fun configure(
        registryLookup: HolderLookup.Provider,
        exporter: SoundExporter
    ) {
        for (sound in NineLifesSounds.all()) {
            exporter.add(sound, SoundTypeBuilder
                .of(sound)
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(sound.location))
            )
        }
    }

    override fun getName(): String = "NineLifesSoundsProvider"
}