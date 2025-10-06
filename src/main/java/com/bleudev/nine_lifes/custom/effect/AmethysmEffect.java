package com.bleudev.nine_lifes.custom.effect;

import com.bleudev.nine_lifes.custom.CustomDamageTypes;
import com.bleudev.nine_lifes.custom.CustomEffects;
import com.bleudev.nine_lifes.networking.payloads.StartAmethysmScreenEffectPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Objects;

public class AmethysmEffect extends StatusEffect {
    private int ticks;
    private LivingEntity entity;

    public AmethysmEffect() {
        super(StatusEffectCategory.NEUTRAL, 0xff00ff);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        if (!world.isClient()) {
            ticks++;
            this.entity = entity;
            if (!entity.isSleeping()) {
                if (ticks % 20 == 0)
                    entity.damage(world, CustomDamageTypes.of(world, CustomDamageTypes.AMETHYSM_DAMAGE_TYPE), 1f);
                entity.setVelocity(0, 0.1, 0);
                entity.velocityModified = true;
            }
            entity.setGlowing(true);
        }
        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);

        ticks = 0;
        this.entity = entity;

        if (entity instanceof ServerPlayerEntity player) try {
            ServerPlayNetworking.send(player, new StartAmethysmScreenEffectPayload(Objects.requireNonNull(player.getStatusEffect(CustomEffects.AMETHYSM)).getDuration()));
        } catch (NullPointerException ignored) {}
    }

    @Override
    public void onRemoved(AttributeContainer attributeContainer) {
        super.onRemoved(attributeContainer);
        if (!entity.hasStatusEffect(StatusEffects.GLOWING))
            entity.setGlowing(false);
    }
}
