package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.custom.NineLifesItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {
    @Shadow
    protected NonNullList<ItemStack> items;

    @Mutable
    @Unique
    @Final
    private @Nullable TagKey<Item> tag;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, RecipeType<?> recipeType, CallbackInfo ci) {
        tag = Map.of(
            RecipeType.SMELTING, NineLifesItemTags.CAUSE_FURNACE_EXPLODE,
            RecipeType.BLASTING, NineLifesItemTags.CAUSE_BLAST_FURNACE_EXPLODE,
            RecipeType.SMOKING, NineLifesItemTags.CAUSE_SMOKER_EXPLODE
        ).get(recipeType);
    }

    @Inject(method = "serverTick", at = @At("RETURN"))
    private static void tick(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, AbstractFurnaceBlockEntity abstractFurnaceBlockEntity, CallbackInfo ci) {
        AbstractFurnaceBlockEntityMixin mixin = (AbstractFurnaceBlockEntityMixin) (Object) abstractFurnaceBlockEntity;
        assert mixin != null;
        NonNullList<ItemStack> inv = mixin.items;
        ItemStack output = inv.get(2);

        if (mixin.tag == null) return;
        if (!output.isEmpty() && output.is(mixin.tag)) {
            inv.set(2, ItemStack.EMPTY);
            serverLevel.removeBlock(blockPos, false);
            Vec3 vec = blockPos.getCenter();
            serverLevel.explode(null, vec.x, vec.y, vec.z, 4f, true, Level.ExplosionInteraction.BLOCK);
        }
    }
}
