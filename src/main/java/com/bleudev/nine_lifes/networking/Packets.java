package com.bleudev.nine_lifes.networking;

import com.bleudev.nine_lifes.networking.payloads.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;

import static com.bleudev.nine_lifes.Nine_lifes.MOD_ID;

public class Packets {
    public static final Identifier UPDATE_CENTER_HEART = Identifier.of(MOD_ID, "update_center_heart");
    public static final Identifier AMETHYSM_EFFECT_UPDATE = Identifier.of(MOD_ID, "amethysm_effect_update");
    public static final Identifier JOIN_MESSAGE = Identifier.of(MOD_ID, "join_message");
    public static final Identifier ARMOR_STAND_HIT_EVENT = Identifier.of(MOD_ID, "armor_stand_hit_event");
    public static final Identifier START_WHITENESS_SCREEN_EVENT = Identifier.of(MOD_ID, "start_whiteness_screen_event");

    public static void initialize() {
        PayloadTypeRegistry.playS2C().register(UpdateCenterHeartPayload.ID, UpdateCenterHeartPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(AmethysmEffectUpdatePayload.ID, AmethysmEffectUpdatePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(JoinMessagePayload.ID, JoinMessagePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ArmorStandHitEventPayload.ID, ArmorStandHitEventPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(StartWhitenessScreenEvent.ID, StartWhitenessScreenEvent.CODEC);
    }
}
