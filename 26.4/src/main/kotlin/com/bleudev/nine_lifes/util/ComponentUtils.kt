package com.bleudev.nine_lifes.util

import com.bleudev.nine_lifes.custom.NineLifesDamageTypeTags
import com.bleudev.nine_lifes.custom.effect.consume.AmethysmConsumeEffect
import net.minecraft.core.RegistryAccess
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.component.Consumables
import net.minecraft.world.item.component.DamageResistant
import net.minecraft.world.item.component.UseCooldown
import net.minecraft.world.item.enchantment.Enchantable
import net.minecraft.world.item.enchantment.ItemEnchantments

internal fun itemEnsureCustomFoods(stack: ItemStack, reg: RegistryAccess): ItemStack {
    if (!stack.`is`(Items.AMETHYST_SHARD)) return stack

    stack.set(DataComponents.FOOD, FoodProperties(3, 0.3f, true))
    stack.set(DataComponents.CONSUMABLE, Consumables.defaultFood().onConsume(AmethysmConsumeEffect.INSTANCE).build())
    stack.set(DataComponents.USE_COOLDOWN, UseCooldown(5f))
    stack.set(DataComponents.ENCHANTABLE, Enchantable(1))
    stack.set(DataComponents.MAX_STACK_SIZE, 65)
    stack.set(DataComponents.DAMAGE_RESISTANT, DamageResistant(reg.get(NineLifesDamageTypeTags.IS_LIGHTNING_OR_FIRE).get()))
    stack.set(DataComponents.RARITY, Rarity.UNCOMMON)
    if (stack.get(DataComponents.ENCHANTMENTS) == null) stack.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
    return stack
}

internal fun playerEnsureCustomFoods(player: ServerPlayer) {
    val inventory = player.inventory
    for (slot in 0..<inventory.nonEquipmentItems.size) {
        val stack = inventory.getItem(slot)
        inventory.setItem(slot, itemEnsureCustomFoods(stack, player.registryAccess()))
    }
}

internal fun itemEntityEnsureCustomFoods(itemEntity: ItemEntity) {
    itemEntity.item = itemEnsureCustomFoods(itemEntity.item, itemEntity.registryAccess())
}
