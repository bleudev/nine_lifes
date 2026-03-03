package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.interfaces.mixin.CustomServerPlayer;
import com.bleudev.nine_lifes.util.InjectsKt;
import com.bleudev.nine_lifes.util.MixinInjectsKt;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements CustomServerPlayer {
    @Unique
    protected int lifes;
    @Unique
    protected int[] lifesPlayTime = new int[9];
    @Override
    public int nl$getLifes() {
        return this.lifes;
    }
    @Override
    public void nl$setLifes(int value) {
        this.lifes = value;
    }
    @Override
    public int nl$getLifesPlayTime(int lifesCount) {
        return this.lifesPlayTime[lifesCount -1];
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    protected void readCustomData(ValueInput valueInput, CallbackInfo ci) {
        this.lifes = valueInput.getInt("lifes").orElse(9);
        this.lifesPlayTime = valueInput.getIntArray("lifesPlayTime").orElse(new int[9]);
    }
    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    protected void writeCustomData(ValueOutput valueOutput, CallbackInfo ci) {
        valueOutput.putInt("lifes", this.lifes);
        valueOutput.putIntArray("lifesPlayTime", this.lifesPlayTime);
    }
    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void onRespawn(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        MixinInjectsKt.setLifes(player, MixinInjectsKt.getLifes(oldPlayer));
        for (int i = 0; i < 9; i++) {
            this.lifesPlayTime[i] = ((CustomServerPlayer) oldPlayer).nl$getLifesPlayTime(i+1);
        }
    }
    @Inject(method = "doTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;awardStat(Lnet/minecraft/resources/Identifier;)V", ordinal = 1, shift = At.Shift.AFTER))
    private void tickLifesPlayTime(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        int totalPlayTime = InjectsKt.getPlayTime(player);
        for (int i = 0; i < 9; i++) {
            if (this.lifesPlayTime[i] > totalPlayTime) this.lifesPlayTime[i] = totalPlayTime;
        }
        int lifesCount = MixinInjectsKt.getLifes(player);
        if (lifesCount > 0) this.lifesPlayTime[lifesCount-1] += 1;
    }
}
