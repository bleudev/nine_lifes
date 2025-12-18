package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.custom.NineLifesItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin {
    @Shadow
    public abstract NonNullList<@NotNull ItemStack> getItems();

    @Shadow @Final
    private int[] cookingProgress;

    @Shadow @Final
    private int[] cookingTime;

    @Inject(method = "cookTick", at = @At("TAIL"))
    private static void explode(
        ServerLevel level, BlockPos pos, BlockState state, CampfireBlockEntity campfire,
        RecipeManager.CachedCheck<@NotNull SingleRecipeInput, @NotNull CampfireCookingRecipe> recipe,
        CallbackInfo ci) {
        var self = (CampfireBlockEntityMixin) (Object) campfire;
        if (self == null) return;
        for (int slot = 0; slot < 4; slot++) {
            ItemStack stack = self.getItems().get(slot);
            if (stack.isEmpty() || !stack.is(NineLifesItemTags.CAUSE_CAMPFIRE_EXPLODE)) continue;
            if (self.cookingProgress[slot] == self.cookingTime[slot] - 1) {
                Vec3 center = pos.getCenter();
                level.removeBlock(pos, false);
                level.explode(null, center.x, center.y, center.z, 4f, true, Level.ExplosionInteraction.BLOCK);
                return;
            }
        }
    }
}

