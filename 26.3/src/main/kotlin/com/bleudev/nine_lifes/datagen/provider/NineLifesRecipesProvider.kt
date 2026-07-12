package com.bleudev.nine_lifes.datagen.provider

import com.bleudev.nine_lifes.custom.NineLifesPotions
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.advancements.Advancement
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.BrewingRecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import java.util.concurrent.CompletableFuture

class NineLifesRecipesProvider(output: FabricPackOutput,
                              providerFuture: CompletableFuture<HolderLookup.Provider>
) : FabricRecipeProvider(output, providerFuture) {
    override fun createRecipeProvider(
        registries: HolderLookup.Provider,
        recipes: BootstrapContext<Recipe<*>>,
        advancements: BootstrapContext<Advancement>
    ): RecipeProvider = NineLifesRecipeProviderImpl(recipes, advancements)

    override fun getName(): String = "NineLifesRecipesProvider"

    private class NineLifesRecipeProviderImpl(recipes: BootstrapContext<Recipe<*>>, advancements: BootstrapContext<Advancement>) : RecipeProvider(recipes, advancements) {
        override fun buildRecipes() {
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(Items.AMETHYST_SHARD), RecipeCategory.MISC, CookingBookCategory.MISC, Items.AMETHYST_SHARD, 0.1f, 100).unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD)).save(output, "blasting_amethyst_shard")
            SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.AMETHYST_SHARD), RecipeCategory.MISC, CookingBookCategory.MISC, Items.AMETHYST_SHARD, 0.1f, 200).unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD)).save(output, "smelting_amethyst_shard")
            SimpleCookingRecipeBuilder.smoking(Ingredient.of(Items.AMETHYST_SHARD), RecipeCategory.MISC, Items.AMETHYST_SHARD, 0.1f, 100).unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD)).save(output, "smoking_amethyst_shard")
            SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(Items.AMETHYST_SHARD), RecipeCategory.MISC, Items.AMETHYST_SHARD, 0.35f, 400).unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD)).save(output, "campfire_cooking_amethyst_shard")

            makePotion(Potions.MUNDANE, NineLifesPotions.AMETHYSM, Items.AMETHYST_SHARD)
            makePotion(Potions.MUNDANE, NineLifesPotions.INSOMNIA, Items.SCULK, Items.SCULK_VEIN, Items.SCULK_SENSOR, Items.SCULK_CATALYST, Items.SCULK_SHRIEKER, Items.CALIBRATED_SCULK_SENSOR)
            makePotion(NineLifesPotions.INSOMNIA, NineLifesPotions.LONGER_INSOMNIA, Items.REDSTONE)
        }

        private fun makePotion(input: Holder<Potion>, output: Holder<Potion>, vararg reagents: Item, ) {
            for (reagent in reagents) {
                BrewingRecipeBuilder.brewingMix(Items.POTION, input, reagent, output).save(this.output)
                BrewingRecipeBuilder.brewingMix(Items.SPLASH_POTION, input, reagent, output).save(this.output)
                BrewingRecipeBuilder.brewingMix(Items.LINGERING_POTION, input, reagent, output).save(this.output)
            }
        }
    }
}
