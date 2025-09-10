package com.bleudev.nine_lifes.client;

import com.bleudev.nine_lifes.ModDataStorage;
import com.bleudev.nine_lifes.networking.payloads.AmethysmEffectUpdatePayload;
import com.bleudev.nine_lifes.networking.payloads.JoinMessagePayload;
import com.bleudev.nine_lifes.networking.payloads.UpdateCenterHeartPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.text.Text;
import org.lwjgl.opengl.GL46;

import static net.minecraft.util.Formatting.DARK_AQUA;
import static net.minecraft.util.Formatting.RED;

public class Nine_lifesClient implements ClientModInitializer {
    private boolean has_effect = false;

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(JoinMessagePayload.ID, ((payload, context) -> {
            boolean careful = payload.lives() <= 5;
            context.player().sendMessage(
                (careful ? Text.translatable("chat.message.join.lives.careful", payload.lives()) :
                Text.translatable("chat.message.join.lives", payload.lives())).formatted(careful ? RED : DARK_AQUA),
                false);
        }));
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
