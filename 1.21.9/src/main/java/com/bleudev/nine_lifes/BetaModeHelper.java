package com.bleudev.nine_lifes;

import com.bleudev.nine_lifes.networking.payloads.BetaModeMessage;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

import static com.bleudev.nine_lifes.NineLifesConst.VERSION;

public class BetaModeHelper {
    public static boolean isInBetaMode() {
        return VERSION.endsWith("_beta");
    }

    public static void trySendBetaModeMessage(ServerPlayerEntity player) {
        if (isInBetaMode())
            ServerPlayNetworking.send(player, new BetaModeMessage());
    }
}
