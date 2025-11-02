package com.bleudev.nine_lifes.interfaces.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public interface BrewingStandBlockEntityCustomInterface {
    DefaultedList<ItemStack> nine_lifes$getInventory();
}
