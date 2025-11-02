package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.custom.CustomTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityMixin {
    @Shadow
    @Final
    private int[] cookingTimes;

    @Shadow
    public abstract DefaultedList<ItemStack> getItemsBeingCooked();

    @Shadow
    @Final
    private int[] cookingTotalTimes;

    @Inject(method = "litServerTick", at = @At("HEAD"))
    private static void explode(ServerWorld world, BlockPos pos, BlockState state, CampfireBlockEntity blockEntity, ServerRecipeManager.MatchGetter<SingleStackRecipeInput, CampfireCookingRecipe> recipeMatchGetter, CallbackInfo ci) {
        var b_entity = (CampfireBlockEntityMixin) (Object) blockEntity;
        if (b_entity != null) {
            for (int slot = 0; slot < 4; slot++) {
                var stack = b_entity.getItemsBeingCooked().get(slot);
                if (stack.equals(ItemStack.EMPTY) || !stack.isIn(CustomTags.ItemTags.CAUSE_CAMPFIRE_EXPLODE)) continue;
                var ticks = b_entity.cookingTotalTimes[slot] - b_entity.cookingTimes[slot];
                if (ticks == 1) {
                    world.breakBlock(pos, false);
                    Vec3d vec = pos.toCenterPos();
                    world.createExplosion(null,
                        vec.getX(), vec.getY(), vec.getZ(),
                        4f, true, World.ExplosionSourceType.BLOCK
                    );
                }
            }
        }
    }
}
