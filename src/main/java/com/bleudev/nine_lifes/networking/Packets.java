package com.bleudev.nine_lifes.networking;

import com.bleudev.nine_lifes.networking.payloads.UpdateCenterHeartPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;

import static com.bleudev.nine_lifes.Nine_lifes.MOD_ID;

public class Packets {
    public static final Identifier UPDATE_CENTER_HEART = Identifier.of(MOD_ID, "update_center_heart");

    public static void initialize() {
        PayloadTypeRegistry.playS2C().register(UpdateCenterHeartPayload.ID, UpdateCenterHeartPayload.CODEC);
    }
}
