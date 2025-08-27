package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.interfaces.mixin.ServerPlayerEntityCustomInteface;
import com.bleudev.nine_lifes.util.LivesUtils;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Unit;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Final;
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
    @Final
    private MinecraftServer server;
    @Shadow
    public ServerPlayNetworkHandler networkHandler;

    @Unique
    protected int lives;
    @Unique
    protected int default_players_percentage;

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
        if (!alive)
            LivesUtils.setLives((ServerPlayerEntity) (Object) this,
                o -> Math.max(((ServerPlayerEntityCustomInteface) oldPlayer).nine_lifes$getLives() - 1, 0));
    }

    @Inject(method = "onDeath", at = @At("RETURN"))
    private void onDeath(DamageSource damageSource, CallbackInfo ci) {
        if (this.nine_lifes$getLives() <= 1)
            this.changeGameMode(GameMode.SPECTATOR);
    }

    @Inject(method = "trySleep", at = @At("HEAD"), cancellable = true)
    private void onTrySleep(BlockPos bedPos, CallbackInfoReturnable<Either<ServerPlayerEntity.SleepFailureReason, Unit>> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        int lives = LivesUtils.getLives(player);

        var psp = server.getGameRules().get(GameRules.PLAYERS_SLEEPING_PERCENTAGE);

        if (lives <= 5) {
            if (psp.get() != 101)
                default_players_percentage = psp.get();
            psp.set(101, server);
        }
        else {
            if (psp.get() == 101)
                psp.set(default_players_percentage, server);
        }

        if (lives <= 3) {
            player.sendMessage(Text.translatable("block.minecraft.bed.no_sleep"), true);
            cir.setReturnValue(Either.left(ServerPlayerEntity.SleepFailureReason.OTHER_PROBLEM));
            cir.cancel();
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void tick(CallbackInfo ci) {
       if (lives <= 3)
           this.networkHandler.sendPacket(new WorldTimeUpdateS2CPacket(6000L, 0, true));
    }
}
