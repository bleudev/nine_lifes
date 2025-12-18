package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.util.createResourceLocation
import net.minecraft.core.Holder
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
        return ResourceKey.create(Registries.ENCHANTMENT, createResourceLocation(name))
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
    class Holders private constructor(private val registryAccess: RegistryAccess) {
        private fun getEntry(key: ResourceKey<Enchantment>): Holder<Enchantment> = registryAccess.getOrThrow(key)

        /**
         * [Holder] of `nine_lifes:charge` enchantment
         *
         * @return holder of enchantment
         */
        fun charge(): Holder<Enchantment> = getEntry(KEY_CHARGE)

        companion object {
            /**
             * Create new [Holders] object from provided [RegistryAccess]
             *
             * @param registryAccess Registry access to use in created object
             * @return new [Holders] object
             */
            @Contract(value = "_ -> new", pure = true)
            fun create(registryAccess: RegistryAccess): Holders = Holders(registryAccess)

            /**
             * [Holder] of `nine_lifes:charge` enchantment
             *
             * @param registryAccess Registry access where to get entry
             * @return holder of enchantment
             */
            fun charge(registryAccess: RegistryAccess): Holder<Enchantment> = create(registryAccess).charge()
        }
    }
}

