package com.bleudev.nine_lifes.util;

import com.bleudev.nine_lifes.custom.CustomEffects;
import com.bleudev.nine_lifes.interfaces.mixin.ServerPlayerEntityCustomInteface;
import com.bleudev.nine_lifes.networking.payloads.UpdateCenterHeart;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;

import java.util.function.Function;

import static net.minecraft.SharedConstants.TICKS_PER_SECOND;

public class LivesUtils {
    public static final int MAX_LIVES = 9;

    private static int clamp(int lives) {
        return MathHelper.clamp(lives, 0, MAX_LIVES);
    }

    public static int getLives(ServerPlayerEntity player) {
        return clamp(((ServerPlayerEntityCustomInteface) player).nine_lifes$getLives());
    }

    public static void setLives(ServerPlayerEntity player, int new_lives) {
        new_lives = clamp(new_lives);
        ((ServerPlayerEntityCustomInteface) player).nine_lifes$setLives(new_lives);
        ServerPlayNetworking.send(player, new UpdateCenterHeart(new_lives));
    }
    public static void setLives(ServerPlayerEntity player, Function<Integer, Integer> changer) {
        setLives(player, changer.apply(getLives(player)));
    }

    public static void resetLives(ServerPlayerEntity player) {
        setLives(player, MAX_LIVES);
    }

    public static void revive(ServerPlayerEntity player) {
        resetLives(player);
        if (player.isSpectator())
            player.changeGameMode(GameMode.SURVIVAL);
        player.addStatusEffect(new StatusEffectInstance(CustomEffects.AMETHYSM, 5 * TICKS_PER_SECOND, 0));
    }
}
