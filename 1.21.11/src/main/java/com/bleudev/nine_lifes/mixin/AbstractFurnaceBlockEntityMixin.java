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
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {
    @Shadow
    protected NonNullList<@NotNull ItemStack> items;
    @Mutable
    @Unique
    @Final
    protected RecipeType<?> type;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, RecipeType<?> recipeType, CallbackInfo ci) {
        type = recipeType;
    }

    @Inject(method = "serverTick", at = @At("RETURN"))
    private static void tick(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, AbstractFurnaceBlockEntity abstractFurnaceBlockEntity, CallbackInfo ci) {
        final var mixin = (AbstractFurnaceBlockEntityMixin) (Object) abstractFurnaceBlockEntity;
        assert mixin != null;
        final var inv = mixin.items;
        ItemStack output = inv.get(2);

        var tags = new HashMap<RecipeType<?>, TagKey<Item>>();
        tags.put(RecipeType.SMELTING, NineLifesItemTags.CAUSE_FURNACE_EXPLODE);
        tags.put(RecipeType.BLASTING, NineLifesItemTags.CAUSE_BLAST_FURNACE_EXPLODE);
        tags.put(RecipeType.SMOKING, NineLifesItemTags.CAUSE_SMOKER_EXPLODE);

        if (!tags.containsKey(mixin.type)) return;
        if (!output.isEmpty() && output.is(tags.get(mixin.type))) {
            inv.set(2, ItemStack.EMPTY);
            serverLevel.removeBlock(blockPos, false);
            var vec = blockPos.getCenter();
            serverLevel.explode(null, vec.x(), vec.y(), vec.z(), 4f, true, Level.ExplosionInteraction.BLOCK);
        }
    }
}
