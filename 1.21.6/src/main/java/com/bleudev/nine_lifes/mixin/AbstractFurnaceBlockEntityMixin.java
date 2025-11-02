package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.custom.CustomTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {
    @Shadow
    protected DefaultedList<ItemStack> inventory;

    @Mutable
    @Unique
    @Final
    protected RecipeType type;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(BlockEntityType blockEntityType, BlockPos pos, BlockState state, RecipeType recipeType, CallbackInfo ci) {
        type = recipeType;
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private static void tick(ServerWorld world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        var mixin = (AbstractFurnaceBlockEntityMixin) (Object) blockEntity;
        assert mixin != null;
        DefaultedList<ItemStack> inv = mixin.inventory;
        ItemStack output = inv.get(2);

        var tags = new HashMap<RecipeType<?>, TagKey<Item>>();
        tags.put(RecipeType.SMELTING, CustomTags.ItemTags.CAUSE_FURNACE_EXPLODE);
        tags.put(RecipeType.BLASTING, CustomTags.ItemTags.CAUSE_BLAST_FURNACE_EXPLODE);
        tags.put(RecipeType.SMOKING, CustomTags.ItemTags.CAUSE_SMOKER_EXPLODE);

        if (!tags.containsKey(mixin.type)) return;

        if (!output.isEmpty() && output.isIn(tags.get(mixin.type))) {
            inv.set(2, ItemStack.EMPTY);
            world.breakBlock(pos, false);
            Vec3d vec = pos.toCenterPos();
            world.createExplosion(null,
                vec.getX(), vec.getY(), vec.getZ(),
                4f, true, World.ExplosionSourceType.BLOCK
            );
        }
    }
}
