package com.bleudev.nine_lifes.client;

import com.bleudev.nine_lifes.ModDataStorage;
import com.bleudev.nine_lifes.networking.payloads.UpdateCenterHeartPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class Nine_lifesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
//        ServerPlayerEvents.JOIN.register(player -> {
//            ModDataStorage.lives = LivesUtils.getLives(player);
//        });
        ClientPlayNetworking.registerGlobalReceiver(UpdateCenterHeartPayload.ID, (payload, context) -> {
            ModDataStorage.lives = payload.lives();
        });
    }
}
