package com.bleudev.nine_lifes.util

import com.bleudev.nine_lifes.custom.NineLifesDamageTypeTags
import com.bleudev.nine_lifes.custom.effect.consume.AmethysmConsumeEffect
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.component.Consumables
import net.minecraft.world.item.component.DamageResistant
import net.minecraft.world.item.component.UseCooldown
import net.minecraft.world.item.enchantment.Enchantable
import net.minecraft.world.item.enchantment.ItemEnchantments

private class CheckStackPredicateBuilder private constructor(private val predicate: P<ItemStack>) {
    fun of(item: Item): CheckStackPredicateBuilder = of(this.predicate.and({ stack -> stack.`is`(item) }))
    fun <T> anotherComponent(type: DataComponentType<T>, component: T): CheckStackPredicateBuilder = of(
        this.predicate.or({ stack -> stack.get(type) == component }))
    fun <T> orNoComponent(type: DataComponentType<T>): CheckStackPredicateBuilder = of(
        this.predicate.or({ stack -> stack.getComponents().get(type) != null }))

    fun with(predicate: P<ItemStack>): CheckStackPredicateBuilder = of(this.predicate.and(predicate))
    fun with(builder: CheckStackPredicateBuilder): CheckStackPredicateBuilder = with(builder.build())
    fun build(): P<ItemStack> = this.predicate

    companion object {
        fun of(predicate: P<ItemStack>): CheckStackPredicateBuilder = CheckStackPredicateBuilder(predicate)
        fun create(): CheckStackPredicateBuilder = of(alwaysTrue())
    }
}

private val amethyst_shard_food_component = FoodProperties(3, 0.3f, true)
private val amethyst_shard_consumable_component = Consumables.defaultFood().onConsume(AmethysmConsumeEffect()).build()
private val amethyst_shard_cooldown_component = UseCooldown(5f)
private val amethyst_enchantable_component = Enchantable(1)
private const val amethyst_max_stack_size_component = 65
private val amethyst_damage_resistant_component = DamageResistant(NineLifesDamageTypeTags.IS_LIGHTNING_OR_FIRE)

fun shouldUpdateAmethystShard(stack: ItemStack): Boolean = CheckStackPredicateBuilder.create()
    .of(Items.AMETHYST_SHARD)
    .with(
        CheckStackPredicateBuilder.create()
            .anotherComponent(DataComponents.FOOD, amethyst_shard_food_component)
            .anotherComponent(DataComponents.CONSUMABLE, amethyst_shard_consumable_component)
            .anotherComponent(DataComponents.USE_COOLDOWN, amethyst_shard_cooldown_component)
            .anotherComponent(DataComponents.ENCHANTABLE, amethyst_enchantable_component)
            .anotherComponent(DataComponents.MAX_STACK_SIZE, amethyst_max_stack_size_component)
            .anotherComponent(DataComponents.DAMAGE_RESISTANT, amethyst_damage_resistant_component)
            .orNoComponent(DataComponents.ENCHANTMENTS)
    ).build()(stack)

fun itemEnsureCustomFoods(stack: ItemStack): ItemStack {
    stack.set(DataComponents.FOOD, amethyst_shard_food_component)
    stack.set(DataComponents.CONSUMABLE, amethyst_shard_consumable_component)
    stack.set(DataComponents.USE_COOLDOWN, amethyst_shard_cooldown_component)
    stack.set(DataComponents.ENCHANTABLE, amethyst_enchantable_component)
    stack.set(DataComponents.MAX_STACK_SIZE, amethyst_max_stack_size_component)
    stack.set(DataComponents.DAMAGE_RESISTANT, amethyst_damage_resistant_component)
    stack.set(DataComponents.RARITY, Rarity.UNCOMMON)
    if (stack.get(DataComponents.ENCHANTMENTS) == null) stack.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
    return stack
}

fun playerEnsureCustomFoods(player: ServerPlayer) {
    val inventory = player.getInventory()
    for (slot in 0..<inventory.nonEquipmentItems.size) {
        val stack = inventory.getItem(slot)
        if (shouldUpdateAmethystShard(stack)) inventory.setItem(slot, itemEnsureCustomFoods(stack))
    }
}