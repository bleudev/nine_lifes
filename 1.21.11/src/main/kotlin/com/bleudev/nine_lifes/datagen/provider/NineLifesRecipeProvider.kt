package com.bleudev.nine_lifes.datagen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import java.util.concurrent.CompletableFuture

class NineLifesRecipesProvider(output: FabricDataOutput,
                              providerFuture: CompletableFuture<HolderLookup.Provider>
) : FabricRecipeProvider(output, providerFuture) {
    override fun createRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput): RecipeProvider = NineLifesRecipeProviderImpl(registries, output)

    override fun getName(): String = "NineLifesRecipesProvider"

    private class NineLifesRecipeProviderImpl(registries: HolderLookup.Provider, output: RecipeOutput) : RecipeProvider(registries, output) {
        override fun buildRecipes() {
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(Items.AMETHYST_SHARD), RecipeCategory.MISC, Items.AMETHYST_SHARD, 0.1f, 100).unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD)).save(output, "blasting_amethyst_shard")
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.AMETHYST_SHARD), RecipeCategory.MISC, Items.AMETHYST_SHARD, 0.1f, 200).unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD)).save(output, "smelting_amethyst_shard")
            SimpleCookingRecipeBuilder.smoking(Ingredient.of(Items.AMETHYST_SHARD), RecipeCategory.MISC, Items.AMETHYST_SHARD, 0.1f, 100).unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD)).save(output, "smoking_amethyst_shard")
            SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(Items.AMETHYST_SHARD), RecipeCategory.MISC, Items.AMETHYST_SHARD, 0.35f, 400).unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD)).save(output, "campfire_cooking_amethyst_shard")
        }
    }
}
