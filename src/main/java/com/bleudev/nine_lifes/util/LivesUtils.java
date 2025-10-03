package com.bleudev.nine_lifes.util;

import com.bleudev.nine_lifes.ServerDataStorage;
import com.bleudev.nine_lifes.networking.payloads.UpdateCenterHeartPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.function.Function;

import static com.bleudev.nine_lifes.custom.CustomGameRules.*;

public class LivesUtils {
    public static final int MAX_LIVES = 9;

    private static int clamp(int lives) {
        return MathHelper.clamp(lives, 0, MAX_LIVES);
    }

    public static int getLives(String nick) {
        return ServerDataStorage.get_lives_of(nick);
    }

    public static void setLives(ServerPlayerEntity player, int new_lives, boolean check_game_rule) {
        new_lives = clamp(new_lives);
        if (check_game_rule) {
            var world = player.getWorld();
            if (!world.getGameRules().getBoolean(DO_LIFES_DECREASE_IN_OVERWORLD) &&
                    world.getRegistryKey().equals(World.OVERWORLD)) return;
            if (!world.getGameRules().getBoolean(DO_LIFES_DECREASE_IN_THE_NETHER) &&
                    world.getRegistryKey().equals(World.NETHER)) return;
            if (!world.getGameRules().getBoolean(DO_LIFES_DECREASE_IN_THE_END) &&
                    world.getRegistryKey().equals(World.END)) return;
        }
        ServerDataStorage.set_lives_of(player.getName().getString(), new_lives);
        ServerPlayNetworking.send(player, new UpdateCenterHeartPayload(new_lives));
    }

    public static void setLives(ServerPlayerEntity player, Function<Integer, Integer> changer, boolean check_game_rule) {
        setLives(player, changer.apply(getLives(player.getName().getString())), check_game_rule);
    }
    public static void setLives(ServerPlayerEntity player, Function<Integer, Integer> changer) {
        setLives(player, changer, true);
    }

    public static void decreaseLives(ServerPlayerEntity player) {
        setLives(player, o -> o - 1);
    }
    public static void resetLives(ServerPlayerEntity player) {
        setLives(player, MAX_LIVES, false);
    }
}
