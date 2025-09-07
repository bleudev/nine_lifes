package com.bleudev.nine_lifes.custom.effect;

import com.bleudev.nine_lifes.custom.CustomDamageTypes;
import com.bleudev.nine_lifes.networking.payloads.AmethysmEffectUpdatePayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class AmethysmEffect extends StatusEffect {
    private int ticks;
    private LivingEntity entity;
    private ServerPlayerEntity player = null;

    public AmethysmEffect() {
        super(StatusEffectCategory.NEUTRAL, 0xff00ff);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        if (!world.isClient) {
            ticks++;
            this.entity = entity;
            if (entity instanceof PlayerEntity player1) {
                System.out.println("is player!");
                player = (ServerPlayerEntity) player1;
                ServerPlayNetworking.send(player, new AmethysmEffectUpdatePayload(true));
            }
            if (!entity.isSleeping()) {
                if (ticks % 20 == 0)
                    entity.damage(world, CustomDamageTypes.of(world, CustomDamageTypes.AMETHYSM_DAMAGE_TYPE), 0.5f);
                entity.setVelocity(0, 0.09, 0);
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

        if (entity instanceof PlayerEntity player1)
            player = (ServerPlayerEntity) player1;
    }

    @Override
    public void onRemoved(AttributeContainer attributeContainer) {
        super.onRemoved(attributeContainer);

        if (!entity.hasStatusEffect(StatusEffects.GLOWING))
            entity.setGlowing(false);

        if (player != null)
            ServerPlayNetworking.send(player, new AmethysmEffectUpdatePayload(false));
    }
}
