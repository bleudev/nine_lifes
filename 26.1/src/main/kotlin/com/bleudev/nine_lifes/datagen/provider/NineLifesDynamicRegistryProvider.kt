package com.bleudev.nine_lifes.datagen.provider

import com.bleudev.nine_lifes.MOD_ID
import com.bleudev.nine_lifes.custom.NineLifesDamageTypes
import com.bleudev.nine_lifes.custom.NineLifesEnchantments
import com.bleudev.nine_lifes.custom.NineLifesItemTags
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider
import net.minecraft.ChatFormatting
import net.minecraft.core.HolderLookup
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.enchantment.Enchantment
import java.util.concurrent.CompletableFuture


class NineLifesDynamicRegistryProvider(output: FabricPackOutput,
                                       registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricDynamicRegistryProvider(output, registriesFuture) {
    override fun configure(registries: HolderLookup.Provider, entries: Entries) {
        entries.addAll(registries.lookupOrThrow(Registries.ENCHANTMENT))
        entries.addAll(registries.lookupOrThrow(Registries.DAMAGE_TYPE))
    }

    override fun getName(): String = MOD_ID

    companion object {
        private fun Enchantment.EnchantmentDefinition.register(context: BootstrapContext<Enchantment>, key: ResourceKey<Enchantment>, componentTransformer: MutableComponent.() -> MutableComponent, exclusiveSet: HolderSet<Enchantment> = HolderSet.empty()) {
            context.register(key, Enchantment(
                Component.translatable("enchantment.nine_lifes.${key.identifier().path}").componentTransformer(),
                this,
                exclusiveSet,
                DataComponentMap.builder().build()
            ))
        }

        fun bootstrapEnchantments(context: BootstrapContext<Enchantment>) {
            Enchantment.definition(
                context.lookup(Registries.ITEM).getOrThrow(NineLifesItemTags.Enchantable.CHARGE),
                context.lookup(Registries.ITEM).getOrThrow(NineLifesItemTags.Enchantable.CHARGE_IN_TABLE),
                1, 1, Enchantment.dynamicCost(3, 0), Enchantment.dynamicCost(7, 0), 4, EquipmentSlotGroup.HAND
            ).register(context, NineLifesEnchantments.KEY_CHARGE, { withStyle(ChatFormatting.WHITE) })
        }

        fun bootstrapDamageTypes(context: BootstrapContext<DamageType>) {
            context.register(NineLifesDamageTypes.AMETHYSM, DamageType("amethysm", 0f))
            context.register(NineLifesDamageTypes.CHARGED_AMETHYST, DamageType("charged_amethyst", 0f))
            context.register(NineLifesDamageTypes.UNKNOWN, DamageType("unknown", 0f))
        }
    }
}