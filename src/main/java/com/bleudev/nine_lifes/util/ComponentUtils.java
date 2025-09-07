package com.bleudev.nine_lifes.util;

import com.bleudev.nine_lifes.custom.consume.AmethysmConsumeEffect;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.UseCooldownComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

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

        public <T> CheckStackPredicateBuilder  not_component(ComponentType<T> type, T component) {
            return of(this.predicate.or(stack -> Objects.requireNonNullElse(stack.get(type), "").equals(component)));
        }

//        public CheckStackPredicateBuilder not_components(List<ComponentData> components) {
//            CheckStackPredicateBuilder builder = create();
//
//            final Predicate<ItemStack>[] new_predicate = new Predicate[]{stack -> true};
//
//            components.forEach(data -> {
//                 new_predicate[0] = new_predicate[0].or(stack -> Objects.requireNonNullElse(stack.get(data.type()), "").equals(data.component()));
//            });
//
//            return of(this.predicate.and(new_predicate[0]));
//        }

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

    public static void ensure_custom_foods(ServerPlayerEntity player) {
        var inventory = player.getInventory();

        for (int slot = 0; slot < inventory.getMainStacks().size(); slot++) {
            ItemStack stack = inventory.getStack(slot);

            FoodComponent amethyst_shard_food_component = new FoodComponent(3, 0.3f, true);
            ConsumableComponent amethyst_shard_consumable_component = ConsumableComponents.food().consumeEffect(new AmethysmConsumeEffect()).build();
            UseCooldownComponent amethyst_shard_cooldown_component = new UseCooldownComponent(5);

            if (CheckStackPredicateBuilder.create()
                .of(Items.AMETHYST_SHARD)
                .with(CheckStackPredicateBuilder.create()
                    .not_component(DataComponentTypes.FOOD, amethyst_shard_food_component)
                    .not_component(DataComponentTypes.CONSUMABLE, amethyst_shard_consumable_component)
                    .not_component(DataComponentTypes.USE_COOLDOWN, amethyst_shard_cooldown_component)
                )
                .build().test(stack)) {
                stack.set(DataComponentTypes.FOOD, amethyst_shard_food_component);
                stack.set(DataComponentTypes.CONSUMABLE, amethyst_shard_consumable_component);
                stack.set(DataComponentTypes.USE_COOLDOWN, amethyst_shard_cooldown_component);
                inventory.setStack(slot, stack);
            }
        }
    }
}
