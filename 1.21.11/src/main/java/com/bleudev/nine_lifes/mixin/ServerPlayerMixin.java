package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.custom.NineLifesMobEffects;
import com.bleudev.nine_lifes.interfaces.mixin.CustomServerPlayer;
import com.bleudev.nine_lifes.util.InjectsKt;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements CustomServerPlayer {
    @Unique
    protected int lifes;
    public int nl$getLifes() {
        return this.lifes;
    }
    public void nl$setLifes(int value) {
        this.lifes = value;
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    protected void readCustomData(ValueInput valueInput, CallbackInfo ci) {
        this.lifes = valueInput.getInt("lifes").orElse(9);
    }
    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    protected void writeCustomData(ValueOutput valueOutput, CallbackInfo ci) {
        valueOutput.putInt("lifes", this.lifes);
    }
    @Inject(method = "restoreFrom", at = @At("TAIL"))
    private void onRespawn(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) {
        var player = (ServerPlayer) (Object) this;
        InjectsKt.setLifes(player, InjectsKt.getLifes(oldPlayer));
    }

    @Inject(method = "startSleepInBed", at = @At("HEAD"), cancellable = true)
    private void onTrySleep(BlockPos blockPos, CallbackInfoReturnable<Either<Player.BedSleepingProblem, net.minecraft.util.Unit>> cir) {
        var player = (ServerPlayer) (Object) this;
        int lifes = InjectsKt.getLifes(player);

        if (!player.hasEffect(NineLifesMobEffects.AMETHYSM)) {
            if (lifes <= 3) {
                cir.setReturnValue(Either.left(Player.BedSleepingProblem.OTHER_PROBLEM));
                player.displayClientMessage(Component.translatable("block.minecraft.bed.no_sleep"), true);
                cir.cancel();
            } else if (lifes <= 5) {
                cir.setReturnValue(Either.left(Player.BedSleepingProblem.NOT_SAFE));
                cir.cancel();
            }
        }
    }
}
