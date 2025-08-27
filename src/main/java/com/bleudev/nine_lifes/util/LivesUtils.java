package com.bleudev.nine_lifes.util;

import com.bleudev.nine_lifes.interfaces.mixin.ServerPlayerEntityCustomInteface;
import com.bleudev.nine_lifes.networking.payloads.UpdateCenterHeartPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Function;

public class LivesUtils {
    public static int getLives(ServerPlayerEntity player) {
        return ((ServerPlayerEntityCustomInteface) player).nine_lifes$getLives();
    }

    public static void setLives(ServerPlayerEntity player, int new_lives) {
        ((ServerPlayerEntityCustomInteface) player).nine_lifes$setLives(new_lives);
        ServerPlayNetworking.send(player, new UpdateCenterHeartPayload(new_lives));
    }
    public static void setLives(ServerPlayerEntity player, Function<Integer, Integer> changer) {
        setLives(player, Math.max(changer.apply(getLives(player)), 0));
    }

    public static void decreaseLives(ServerPlayerEntity player) {
        setLives(player, o -> o - 1);
    }
    public static void resetLives(ServerPlayerEntity player) {
        setLives(player, o -> 9);
    }
}
