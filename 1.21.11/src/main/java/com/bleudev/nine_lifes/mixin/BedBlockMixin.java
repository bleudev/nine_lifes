package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.NineLifes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;

@Mixin(BedBlock.class)
public class BedBlockMixin {
    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    private void cancelUseIfAnaglyph(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (level.isClientSide()) return;
        String name = player.getGameProfile().name();
        HashMap<String, Integer> map = NineLifes.getNotSafeSleepTicks();
        if (map.containsKey(name)) {
            if (map.get(name) > 0) cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
