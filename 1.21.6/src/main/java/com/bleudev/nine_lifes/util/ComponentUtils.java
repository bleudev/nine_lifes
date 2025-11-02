package com.bleudev.nine_lifes.util;

import com.bleudev.nine_lifes.custom.CustomTags;
import com.bleudev.nine_lifes.custom.consume.AmethysmConsumeEffect;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Rarity;

import java.util.Objects;
import java.util.function.Predicate;

public class ComponentUtils {
    private static class CheckStackPredicateBuilder {
        private Predicate<ItemStack> predicate;
        private CheckStackPredicateBuilder(Predicate<ItemStack> predicate) {
            this.predicate = predicate;
        }

        public static CheckStackPredicateBuilder of(Predicate<ItemStack> predicate) {
            return new CheckStackPredicateBuilder(predicate);
        }

        public static CheckStackPredicateBuilder create() {
            return of(stack -> true);
        }

        public CheckStackPredicateBuilder of(Item item) {
            return of(this.predicate.and(stack -> stack.isOf(item)));
        }

        public <T> CheckStackPredicateBuilder another_component(ComponentType<T> type, T component) {
            return of(this.predicate.or(stack -> Objects.requireNonNullElse(stack.get(type), "").equals(component)));
        }

        public <T> CheckStackPredicateBuilder or_no_component(ComponentType<T> type) {
            return of(this.predicate.or(stack -> stack.getComponents().contains(type)));
        }

        public CheckStackPredicateBuilder with(Predicate<ItemStack> predicate) {
            return of(this.predicate.and(predicate));
        }

        public CheckStackPredicateBuilder with(CheckStackPredicateBuilder builder) {
            return with(builder.build());
        }

        public Predicate<ItemStack> build() {
            return this.predicate;
        }
    }

    private static final FoodComponent amethyst_shard_food_component = new FoodComponent(3, 0.3f, true);
    private static final ConsumableComponent amethyst_shard_consumable_component = ConsumableComponents.food().consumeEffect(new AmethysmConsumeEffect()).build();
    private static final UseCooldownComponent amethyst_shard_cooldown_component = new UseCooldownComponent(5);
    private static final EnchantableComponent amethyst_enchantable_component = new EnchantableComponent(1);
    private static final Integer amethyst_max_stack_size_component = 65;
    private static final DamageResistantComponent amethyst_damage_resistant_component = new DamageResistantComponent(CustomTags.DamageTypeTags.IS_LIGHTNING_OR_FIRE);

    public static boolean should_update_amethyst_shard(ItemStack stack) {
        return CheckStackPredicateBuilder.create()
            .of(Items.AMETHYST_SHARD)
            .with(CheckStackPredicateBuilder.create()
                .another_component(DataComponentTypes.FOOD, amethyst_shard_food_component)
                .another_component(DataComponentTypes.CONSUMABLE, amethyst_shard_consumable_component)
                .another_component(DataComponentTypes.USE_COOLDOWN, amethyst_shard_cooldown_component)
                .another_component(DataComponentTypes.ENCHANTABLE, amethyst_enchantable_component)
                .another_component(DataComponentTypes.MAX_STACK_SIZE, amethyst_max_stack_size_component)
                .another_component(DataComponentTypes.DAMAGE_RESISTANT, amethyst_damage_resistant_component)
                .or_no_component(DataComponentTypes.ENCHANTMENTS)
            )
        .build().test(stack);
    }

    public static ItemStack item_ensure_custom_foods(ItemStack stack) {
        stack.set(DataComponentTypes.FOOD, amethyst_shard_food_component);
        stack.set(DataComponentTypes.CONSUMABLE, amethyst_shard_consumable_component);
        stack.set(DataComponentTypes.USE_COOLDOWN, amethyst_shard_cooldown_component);
        stack.set(DataComponentTypes.ENCHANTABLE, amethyst_enchantable_component);
        stack.set(DataComponentTypes.MAX_STACK_SIZE, amethyst_max_stack_size_component);
        stack.set(DataComponentTypes.DAMAGE_RESISTANT, amethyst_damage_resistant_component);
        stack.set(DataComponentTypes.RARITY, Rarity.UNCOMMON);
        if (!stack.getComponents().contains(DataComponentTypes.ENCHANTMENTS))
            stack.set(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
        return stack;
    }

    public static void player_ensure_custom_foods(ServerPlayerEntity player) {
        var inventory = player.getInventory();

        for (int slot = 0; slot < inventory.getMainStacks().size(); slot++) {
            ItemStack stack = inventory.getStack(slot);
            if (should_update_amethyst_shard(stack))
                inventory.setStack(slot, item_ensure_custom_foods(stack));
        }
    }
}
