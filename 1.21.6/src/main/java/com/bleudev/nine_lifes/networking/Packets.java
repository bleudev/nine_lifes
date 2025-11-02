package com.bleudev.nine_lifes.networking;

import com.bleudev.nine_lifes.networking.payloads.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.util.Identifier;

import static com.bleudev.nine_lifes.Nine_lifes.MOD_ID;

public class Packets {
    public static final Identifier UPDATE_CENTER_HEART = Identifier.of(MOD_ID, "update_center_heart");
    public static final Identifier JOIN_MESSAGE = Identifier.of(MOD_ID, "join_message");
    public static final Identifier ARMOR_STAND_HIT_EVENT = Identifier.of(MOD_ID, "armor_stand_hit_event");
    public static final Identifier START_WHITENESS_SCREEN_EVENT = Identifier.of(MOD_ID, "start_whiteness_screen_event");
    public static final Identifier START_AMETHYSM_SCREEN_EFFECT = Identifier.of(MOD_ID, "start_amethysm_screen_effect");
    public static final Identifier START_CHARGE_SCREEN_EFFECT = Identifier.of(MOD_ID, "start_charge_screen_effect");

    public static void initialize() {
        PayloadTypeRegistry.playS2C().register(UpdateCenterHeart.ID, UpdateCenterHeart.CODEC);
        PayloadTypeRegistry.playS2C().register(JoinMessage.ID, JoinMessage.CODEC);
        PayloadTypeRegistry.playS2C().register(ArmorStandHitEvent.ID, ArmorStandHitEvent.CODEC);
        PayloadTypeRegistry.playS2C().register(StartWhitenessScreenEvent.ID, StartWhitenessScreenEvent.CODEC);
        PayloadTypeRegistry.playS2C().register(StartAmethysmScreenEffect.ID, StartAmethysmScreenEffect.CODEC);
        PayloadTypeRegistry.playS2C().register(StartChargeScreenEffect.ID, StartChargeScreenEffect.CODEC);
    }
}
