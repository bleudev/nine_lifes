package com.bleudev.nine_lifes;

import com.bleudev.nine_lifes.custom.CustomDamageTypes;
import com.bleudev.nine_lifes.custom.CustomEffects;
import com.bleudev.nine_lifes.interfaces.mixin.BrewingStandBlockEntityCustomInterface;
import com.bleudev.nine_lifes.util.WorldUtils;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Objects;

public class WindChargeTickFeatures {
    public static void do_for(ServerWorld world, WindChargeEntity wind_charge) {
        Box action_box = Box.of(wind_charge.getPos(), 3, 3, 3);
        WorldUtils.forBlocksInBox(action_box, (pos) -> {
            var blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BrewingStandBlockEntity brewing) {
                var inventory = ((BrewingStandBlockEntityCustomInterface) brewing).nine_lifes$getInventory();

                if (inventory.subList(0, 3).stream().noneMatch(potion -> {
                    try {
                        for (var eff: Objects.requireNonNull(potion.get(DataComponentTypes.POTION_CONTENTS)).getEffects())
                            if (eff.getEffectType().equals(CustomEffects.AMETHYSM))
                                return true;
                        return false;
                    } catch (NullPointerException ignored) { return false; }
                })) return;

                world.breakBlock(pos, false);
                world.createExplosion(
                        null, CustomDamageTypes.of(world, CustomDamageTypes.CHARGED_AMETHYST_DAMAGE_TYPE), null,
                        pos.getX(), pos.getY(), pos.getZ(),
                        3f, true, World.ExplosionSourceType.BLOCK
                );
            }
        });

        System.out.println(TypeFilter.instanceOf(LivingEntity.class));

        world.getEntitiesByClass(LivingEntity.class, action_box, ignored -> true).forEach(entity -> {
            entity.removeStatusEffect(CustomEffects.AMETHYSM);
        });

        world.getPlayers().forEach(player -> {
            if (!action_box.contains(player.getPos())) return;
            var inventory = player.getInventory();
            var inventory_updated = false;
            for (int slot = 0; slot < inventory.getMainStacks().size(); slot++) {
                var stack = inventory.getStack(slot);
                var new_stack = stack.copy();

                PotionContentsComponent potion;
                if ((potion = stack.get(DataComponentTypes.POTION_CONTENTS)) != null)
                    for (var effect: potion.getEffects())
                        if (effect.getEffectType().equals(CustomEffects.AMETHYSM)) {
                            new_stack = ItemStack.EMPTY;
                            break;
                        }

                if (!ItemStack.areEqual(stack, new_stack)) {
                    inventory.setStack(slot, new_stack);
                    inventory_updated = true;
                }
            }

            if (inventory_updated)
                player.sendMessage(Text.of("Broke potions!"), false);
        });
    }
}
