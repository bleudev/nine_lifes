package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.interfaces.mixin.BrewingStandBlockEntityCustomInterface;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BrewingStandBlockEntity.class)
public class BrewingStandBlockEntityMixin implements BrewingStandBlockEntityCustomInterface {
    @Shadow
    private DefaultedList<ItemStack> inventory;

    @Unique
    public DefaultedList<ItemStack> nine_lifes$getInventory() {
        return inventory;
    }
}
