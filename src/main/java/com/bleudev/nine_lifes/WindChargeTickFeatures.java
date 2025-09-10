package com.bleudev.nine_lifes;

import com.bleudev.nine_lifes.custom.CustomDamageTypes;
import com.bleudev.nine_lifes.custom.CustomEffects;
import com.bleudev.nine_lifes.interfaces.mixin.BrewingStandBlockEntityCustomInterface;
import com.bleudev.nine_lifes.util.WorldUtils;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.projectile.WindChargeEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Objects;

public class WindChargeTickFeatures {
    public static void do_for(ServerWorld world, WindChargeEntity wind_charge) {
        // TODO: Break amethysm potions in player inventory
        // TODO: Clear entity amethysm effect
        WorldUtils.forBlocksInBox(Box.of(wind_charge.getPos(), 3, 3, 3), (pos) -> {
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
    }
}
