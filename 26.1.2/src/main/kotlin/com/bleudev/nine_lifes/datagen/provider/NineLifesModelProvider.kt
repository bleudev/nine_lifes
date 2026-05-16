package com.bleudev.nine_lifes.datagen.provider

import com.bleudev.nine_lifes.custom.NineLifesItems
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.model.ModelTemplates

class NineLifesModelProvider(output: FabricPackOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(blockModelGenerators: BlockModelGenerators) {
    }

    override fun generateItemModels(itemModelGenerators: ItemModelGenerators) {
        itemModelGenerators.generateFlatItem(NineLifesItems.AMETHYST_STICK, ModelTemplates.FLAT_HANDHELD_ITEM)
    }

}