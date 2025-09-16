package com.bleudev.nine_lifes.util;

import com.bleudev.nine_lifes.interfaces.mixin.ServerPlayerEntityCustomInteface;
import com.bleudev.nine_lifes.networking.payloads.UpdateCenterHeartPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.function.Function;

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
        ServerPlayNetworking.send(player, new UpdateCenterHeartPayload(new_lives));
    }
    public static void setLives(ServerPlayerEntity player, Function<Integer, Integer> changer) {
        setLives(player, changer.apply(getLives(player)));
    }

    public static void resetLives(ServerPlayerEntity player) {
        setLives(player, MAX_LIVES);
    }
}
