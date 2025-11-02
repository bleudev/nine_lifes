package com.bleudev.nine_lifes.custom;

import com.bleudev.nine_lifes.custom.consume.AmethysmConsumeEffect;
import net.minecraft.item.consume.ConsumeEffect;

import static com.bleudev.nine_lifes.util.RegistryUtils.registerConsumeEffectType;

public class CustomConsumeEffectTypes {
    public static final ConsumeEffect.Type<AmethysmConsumeEffect> AMETHYSM_CONSUME_EFFECT_TYPE = registerConsumeEffectType("amethysm_consume_effect_type", AmethysmConsumeEffect.CODEC, AmethysmConsumeEffect.PACKET_CODEC);

    public static void initialize() {}
}
