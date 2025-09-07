package com.bleudev.nine_lifes.client;

import com.bleudev.nine_lifes.ModDataStorage;
import com.bleudev.nine_lifes.networking.payloads.AmethysmEffectUpdatePayload;
import com.bleudev.nine_lifes.networking.payloads.UpdateCenterHeartPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import org.lwjgl.opengl.GL46;

public class Nine_lifesClient implements ClientModInitializer {
    private boolean has_effect = false;

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(UpdateCenterHeartPayload.ID, (payload, context) -> {
            ModDataStorage.lives = payload.lives();
        });
        ClientPlayNetworking.registerGlobalReceiver(AmethysmEffectUpdatePayload.ID, (payload, context) -> {
            has_effect = payload.has_effect();
        });
        WorldRenderEvents.AFTER_SETUP.register(context -> {
            if (has_effect)
                GL46.glColorMask(false, true, false, true);
            else
                GL46.glColorMask(true, true, true, true);
        });
    }
}
