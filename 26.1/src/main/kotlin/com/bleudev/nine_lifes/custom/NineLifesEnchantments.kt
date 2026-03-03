package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.util.createIdentifier
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment
import org.jetbrains.annotations.Contract

object NineLifesEnchantments {
    /**
     * [ResourceKey] of `nine_lifes:charge` enchantment
     */
    val KEY_CHARGE: ResourceKey<Enchantment> = of("charge")

    private fun of(name: String): ResourceKey<Enchantment> {
        return ResourceKey.create(Registries.ENCHANTMENT, createIdentifier(name))
    }

    /**
     * Do not use in mods depends on Nine Lifes!
     */
    fun initialize() {
    }

    /**
     * Class which provides functions and methods to get enchantment [registry entries][Holder] with provided [RegistryAccess]
     *
     * ```
     * NineLifesEnchantments.Holders.create(world.registryAccess()).charge()
     * // Or
     * NineLifesEnchantments.Holders.charge(world.registryAccess())
     * ```
     */
    class Holders private constructor(private val getter: (ResourceKey<Enchantment>) -> Holder<Enchantment>) {
        /**
         * [Holder] of `nine_lifes:charge` enchantment
         *
         * @return holder of enchantment
         */
        fun charge(): Holder<Enchantment> = getter(KEY_CHARGE)

        companion object {
            /**
             * Create new [Holders] object from provided [HolderGetter.Provider] ([RegistryAccess], [HolderLookup.Provider] etc.)
             *
             * @param provider Provider to use in created object
             * @return new [Holders] object
             */
            @Contract(value = "_ -> new", pure = true)
            fun <T : HolderGetter.Provider> create(provider: T): Holders = Holders(provider::getOrThrow)

            /**
             * Create new [Holders] object from provided enchantment [HolderGetter] (for example, [HolderLookup.RegistryLookup])
             *
             * @param getter Holder getter to use in created object
             * @return new [Holders] object
             * */
            fun <T : HolderGetter<Enchantment>> create(getter: T): Holders = Holders(getter::getOrThrow)

            /**
             * [Holder] of `nine_lifes:charge` enchantment
             *
             * @param provider Provider where to get entry
             * @return holder of enchantment
             */
            fun <T : HolderGetter.Provider> charge(provider: T): Holder<Enchantment> = create(provider).charge()
            /**
             * [Holder] of `nine_lifes:charge` enchantment
             *
             * @param getter Registry enchantment lookup where to get entry
             * @return holder of enchantment
             * */
            fun <T : HolderGetter<Enchantment>> charge(getter: T): Holder<Enchantment> = create(getter).charge()
        }
    }
}

