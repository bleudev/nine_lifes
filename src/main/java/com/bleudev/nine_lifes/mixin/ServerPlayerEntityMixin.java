package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.interfaces.mixin.ServerPlayerEntityCustomInteface;
import com.bleudev.nine_lifes.util.LivesUtils;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements ServerPlayerEntityCustomInteface {
    @Shadow
    public abstract boolean changeGameMode(GameMode gameMode);

    @Unique
    protected int lives;

    public int nine_lifes$getLives() {
        return this.lives;
    }

    public void nine_lifes$setLives(int value) {
        this.lives = value;
    }

    @Inject(method = "readCustomData", at = @At("RETURN"))
    protected void readCustomData(ReadView view, CallbackInfo ci) {
        this.lives = view.getInt("lives", 9);
    }

    @Inject(method = "writeCustomData", at = @At("RETURN"))
    protected void writeCustomData(WriteView view, CallbackInfo ci) {
        view.putInt("lives", this.lives);
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void onRespawn(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        if (!alive) LivesUtils.setLives((ServerPlayerEntity) (Object) this,
            o -> Math.max(((ServerPlayerEntityCustomInteface) oldPlayer).nine_lifes$getLives() - 1, 0));
    }

    @Inject(method = "onDeath", at = @At("RETURN"))
    private void onDeath(DamageSource damageSource, CallbackInfo ci) {
        if (this.nine_lifes$getLives() <= 1)
            this.changeGameMode(GameMode.SPECTATOR);
    }
}
