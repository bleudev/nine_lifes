package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.custom.NineLifesMobEffects;
import com.bleudev.nine_lifes.interfaces.mixin.CustomLivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements CustomLivingEntity {
    @Shadow
    public abstract boolean hasEffect(Holder<@NotNull MobEffect> holder);

    @Unique
    private BlockPos lastLightPos;

    @Unique
    private int damage_ticks = -1;
    @Unique
    public void nl$setDamageTicks(int new_damage_ticks) {
        damage_ticks = new_damage_ticks;
    }
    @Unique
    public int nl$getDamageTicks() {
        return damage_ticks;
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    private void readDamageTicks(ValueInput valueInput, CallbackInfo ci) {
        damage_ticks = valueInput.getInt("damage_ticks").orElse(-1);
    }
    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    private void addDamageTicks(ValueOutput valueOutput, CallbackInfo ci) {
        valueOutput.putInt("damage_ticks", damage_ticks);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;

        var level = entity.level();
        if (level.isClientSide()) return;
        boolean shouldLight = shouldEmitLight();
        BlockPos footPos = entity.blockPosition();

        for (var pos: List.of(footPos, footPos.above()))
            if (tryEmitLight((ServerLevel) level, pos, shouldLight ? 15 : 0)) break;
    }

    @Unique
    private void removeLightAt(ServerLevel level, BlockPos pos) {
        if (level.getBlockState(pos).is(Blocks.LIGHT))
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
    }

    @Unique
    private boolean tryEmitLight(ServerLevel level, BlockPos pos, int lightLevel) {
        if (lastLightPos != null && !lastLightPos.equals(pos)) {
            removeLightAt(level, lastLightPos);
            lastLightPos = null;
        }

        if (lightLevel > 0) {
            var existing = level.getBlockState(pos);
            if (existing.isAir() || existing.is(Blocks.LIGHT)) {
                var lightState = Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, lightLevel);
                level.setBlock(pos, lightState, 3);
                lastLightPos = pos;
                return true;
            }
        } else if (lastLightPos != null) {
            removeLightAt(level, lastLightPos);
            lastLightPos = null;
        }
        return false;
    }

    @Inject(method = "remove", at = @At("HEAD"))
    private void remove(Entity.RemovalReason removalReason, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        var level = entity.level();
        if (!level.isClientSide() && lastLightPos != null) {
            removeLightAt((ServerLevel) level, lastLightPos);
            lastLightPos = null;
        }
    }

    @Unique
    private boolean shouldEmitLight() {
        return hasEffect(NineLifesMobEffects.AMETHYSM) || hasEffect(MobEffects.GLOWING);
    }
}
