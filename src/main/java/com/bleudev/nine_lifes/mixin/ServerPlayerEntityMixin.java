package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.custom.CustomEffects;
import com.bleudev.nine_lifes.interfaces.mixin.ServerPlayerEntityCustomInteface;
import com.bleudev.nine_lifes.util.LivesUtils;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Unit;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements ServerPlayerEntityCustomInteface {
    @Shadow
    public abstract boolean changeGameMode(GameMode gameMode);
    @Shadow
    public ServerPlayNetworkHandler networkHandler;

    @Shadow
    public abstract @NotNull GameMode getGameMode();

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
        if (!alive && oldPlayer.getGameMode().isSurvivalLike())
            LivesUtils.setLives((ServerPlayerEntity) (Object) this,
                o -> Math.max(((ServerPlayerEntityCustomInteface) oldPlayer).nine_lifes$getLives() - 1, 0));
    }

    @Inject(method = "onDeath", at = @At("RETURN"))
    private void onDeath(DamageSource damageSource, CallbackInfo ci) {
        if (this.nine_lifes$getLives() <= 1 && getGameMode().isSurvivalLike())
            this.changeGameMode(GameMode.SPECTATOR);
    }

    @Inject(method = "trySleep", at = @At("HEAD"), cancellable = true)
    private void onTrySleep(BlockPos bedPos, CallbackInfoReturnable<Either<ServerPlayerEntity.SleepFailureReason, Unit>> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        int lives = LivesUtils.getLives(player);

        if (!player.hasStatusEffect(CustomEffects.AMETHYSM)) {
            if (lives <= 3) {
                cir.setReturnValue(Either.left(PlayerEntity.SleepFailureReason.NOT_POSSIBLE_NOW));
                cir.cancel();
            } else if (lives <= 5) {
                cir.setReturnValue(Either.left(PlayerEntity.SleepFailureReason.NOT_SAFE));
                cir.cancel();
            }
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        if (lives <= 3 && this.getGameMode().isSurvivalLike())
           this.networkHandler.sendPacket(new WorldTimeUpdateS2CPacket(9000L, 0, true));
    }
}
