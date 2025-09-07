package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.custom.CustomEffects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);

    @Unique
    private BlockPos lastLightPos;

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.getWorld().isClient) return;

        ServerWorld serverWorld = (ServerWorld) entity.getWorld();
        boolean shouldLight = shouldEmitLight();
        int lightLevel = shouldLight ? 15 : 0;
        BlockPos footPos = entity.getBlockPos();

        List<BlockPos> poses = List.of(footPos, footPos.add(0, 1, 0));
        for (var pos: poses)
            if (tryEmitLight(serverWorld, pos, lightLevel)) break;
    }

    @Unique
    private void removeLightAt(ServerWorld world, BlockPos pos) {
        if (world.getBlockState(pos).isOf(Blocks.LIGHT)) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    @Unique
    private boolean tryEmitLight(ServerWorld serverWorld, BlockPos pos, int lightLevel) {
        if (lastLightPos != null && !lastLightPos.equals(pos)) {
            removeLightAt(serverWorld, lastLightPos);
            lastLightPos = null;
        }

        if (lightLevel > 0) {
            BlockState existing = serverWorld.getBlockState(pos);
            if (existing.isAir() || existing.isOf(Blocks.LIGHT)) {
                BlockState lightState = Blocks.LIGHT.getDefaultState().with(Properties.LEVEL_15, lightLevel);
                serverWorld.setBlockState(pos, lightState, 3);
                lastLightPos = pos;
                return true;
            }
        } else if (lastLightPos != null) {
            removeLightAt(serverWorld, lastLightPos);
            lastLightPos = null;
        }
        return false;
    }

    @Inject(method = "remove", at = @At("HEAD"))
    public void remove(Entity.RemovalReason reason, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!entity.getWorld().isClient && lastLightPos != null) {
            removeLightAt((ServerWorld)entity.getWorld(), lastLightPos);
            lastLightPos = null;
        }
    }

    @Unique
    private boolean shouldEmitLight() {
        return hasStatusEffect(CustomEffects.AMETHYSM) || hasStatusEffect(StatusEffects.GLOWING);
    }
}
