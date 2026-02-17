package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.interfaces.mixin.CustomServerPlayer;
import com.bleudev.nine_lifes.util.InjectsKt;
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
}
