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

import static com.bleudev.nine_lifes.NineLifesConstKt.MAX_LIFES;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements CustomServerPlayer {
    @Unique
    protected int lifes;
    @Unique
    protected int[] lifesPlayTime = new int[9];
    @Unique
    protected int stickUsedTicks;
    @Override
    public int nl$getLifes() {
        return this.lifes;
    }
    @Override
    public void nl$setLifes(int newLifesCount) throws IllegalArgumentException {
        if (newLifesCount < 0 || newLifesCount > MAX_LIFES) throw new IllegalArgumentException("Lifes count out of range[0; 9]");
        this.lifes = newLifesCount;
    }
    @Override
    public int nl$getLifesPlayTime(int lifesCount) throws NullPointerException {
        if (lifesCount < 0 || lifesCount > MAX_LIFES) throw new NullPointerException("Lifes count out of range [0; 9]");
        if (lifesCount == 0) return 0;
        return this.lifesPlayTime[lifesCount - 1];
    }
    @Override
    public int nl$getStickUsedTicks() {
        return this.stickUsedTicks;
    }
    @Override
    public void nl$setStickUsedTicks(int newTicks) {
        this.stickUsedTicks = Math.max(newTicks, 0);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    protected void readCustomData(ValueInput input, CallbackInfo ci) {
        this.lifes = input.getInt("lifes").orElse(9);
        this.lifesPlayTime = input.getIntArray("lifesPlayTime").orElse(new int[9]);
        this.stickUsedTicks = input.getInt("stickUsedTicks").orElse(0);
    }
    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    protected void writeCustomData(ValueOutput output, CallbackInfo ci) {
        output.putInt("lifes", this.lifes);
        output.putIntArray("lifesPlayTime", this.lifesPlayTime);
        output.putInt("stickUsedTicks", this.stickUsedTicks);
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
        if (lifesCount > 0) this.lifesPlayTime[lifesCount-1]++;
        if (this.stickUsedTicks > 0) this.stickUsedTicks--;
    }
}
