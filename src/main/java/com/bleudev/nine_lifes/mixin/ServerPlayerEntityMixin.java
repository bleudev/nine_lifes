package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.custom.CustomEffects;
import com.bleudev.nine_lifes.util.LivesUtils;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Unit;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
    @Shadow
    public abstract boolean changeGameMode(GameMode gameMode);
    @Shadow
    public ServerPlayNetworkHandler networkHandler;

    @Shadow
    public abstract @NotNull GameMode getGameMode();

//    @Inject(method = "readCustomData", at = @At("RETURN"))
//    protected void readCustomData(ReadView view, CallbackInfo ci) {
//        // Deprecated. Will be deleted in 2.0
//        var nick = ((ServerPlayerEntity) (Object) this).getName().getString();
//        if (!ServerDataStorage.contains_lives_of(nick))
//            ServerDataStorage.set_lives_of(nick, view.getInt("lives", 9));
//    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void onRespawn(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci) {
        if (!alive && oldPlayer.getGameMode().isSurvivalLike())
            LivesUtils.decreaseLives(oldPlayer);
    }

    @Inject(method = "onDeath", at = @At("RETURN"))
    private void onDeath(DamageSource damageSource, CallbackInfo ci) {
        if (LivesUtils.getLives(((ServerPlayerEntity) (Object) this).getName().getString()) <= 1 && getGameMode().isSurvivalLike())
            this.changeGameMode(GameMode.SPECTATOR);
    }

    @Inject(method = "trySleep", at = @At("HEAD"), cancellable = true)
    private void onTrySleep(BlockPos bedPos, CallbackInfoReturnable<Either<ServerPlayerEntity.SleepFailureReason, Unit>> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        int lives = LivesUtils.getLives(player.getName().getString());

        if (!player.hasStatusEffect(CustomEffects.AMETHYSM)) {
            if (lives <= 3) {
                cir.setReturnValue(Either.left(ServerPlayerEntity.SleepFailureReason.NOT_POSSIBLE_NOW));
                cir.cancel();
            } else if (lives <= 5) {
                cir.setReturnValue(Either.left(PlayerEntity.SleepFailureReason.NOT_SAFE));
                cir.cancel();
            }
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        var effect = player.getStatusEffect(CustomEffects.AMETHYSM);

        if (!player.getGameMode().isSurvivalLike()) return;
        if (LivesUtils.getLives(player.getName().getString()) <= 3 || ((effect != null) && (!effect.isDurationBelow(10))))
           this.networkHandler.sendPacket(new WorldTimeUpdateS2CPacket(9000L, 0, true));
    }
}
