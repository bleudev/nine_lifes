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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin {
    @Shadow
    public abstract NonNullList<ItemStack> getItems();

    @Shadow
    @Final
    private int[] cookingProgress;

    @Shadow
    @Final
    private int[] cookingTime;

    @Inject(method = "cookTick", at = @At("HEAD"))
    private static void explode(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, CampfireBlockEntity campfireBlockEntity, RecipeManager.CachedCheck<SingleRecipeInput, CampfireCookingRecipe> cachedCheck, CallbackInfo ci) {
        var b = (CampfireBlockEntityMixin) (Object) campfireBlockEntity;
        if (b != null) {
            for (int slot = 0; slot < 4; slot++) {
                var stack = b.getItems().get(slot);
                if (stack.equals(ItemStack.EMPTY) || !stack.is(NineLifesItemTags.CAUSE_CAMPFIRE_EXPLODE)) continue;
                var ticks = b.cookingProgress[slot] - b.cookingTime[slot];
                if (ticks == 1) {
                    serverLevel.removeBlock(blockPos, false);
                    Vec3 vec = blockPos.getCenter();
                    serverLevel.explode(null, vec.x(), vec.y(), vec.z(), 4f, true, Level.ExplosionInteraction.BLOCK);
                }
            }
        }
    }
}
