package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.custom.NineLifesDamageSources;
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
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nine_lifes.util.InjectsKt.explode;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin {
    @Shadow
    public abstract NonNullList<@NotNull ItemStack> getItems();

    @Shadow @Final
    private int[] cookingProgress;

    @Shadow @Final
    private int[] cookingTime;

    @Inject(method = "cookTick", at = @At("TAIL"))
    private static void explodeOnEnd(
        ServerLevel level, BlockPos pos, BlockState state, CampfireBlockEntity campfire,
        RecipeManager.CachedCheck<@NotNull SingleRecipeInput, @NotNull CampfireCookingRecipe> recipe,
        CallbackInfo ci) {
        var self = (CampfireBlockEntityMixin) (Object) campfire;
        if (self == null) return;
        for (int slot = 0; slot < 4; slot++) {
            ItemStack stack = self.getItems().get(slot);
            if (stack.isEmpty() || !stack.is(NineLifesItemTags.CAUSE_CAMPFIRE_EXPLODE)) continue;
            if (self.cookingProgress[slot] == self.cookingTime[slot] - 1) {
                level.removeBlock(pos, false);
                explode(level, pos.getCenter(), 4f, NineLifesDamageSources::charged, Level.ExplosionInteraction.BLOCK, null);
                return;
            }
        }
    }
}

