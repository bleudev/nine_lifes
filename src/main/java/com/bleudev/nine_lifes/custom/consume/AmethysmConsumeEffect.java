package com.bleudev.nine_lifes.custom.consume;

import com.bleudev.nine_lifes.custom.CustomEffects;
import com.bleudev.nine_lifes.custom.CustomEnchantments;
import com.bleudev.nine_lifes.interfaces.mixin.LivingEntityCustomInterface;
import com.bleudev.nine_lifes.networking.payloads.StartWhitenessScreenEvent;
import com.bleudev.nine_lifes.util.LivesUtils;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import static com.bleudev.nine_lifes.custom.CustomConsumeEffectTypes.AMETHYSM_CONSUME_EFFECT_TYPE;
import static net.minecraft.SharedConstants.TICKS_PER_SECOND;

public record AmethysmConsumeEffect() implements ConsumeEffect {
    public static final AmethysmConsumeEffect INSTANCE = new AmethysmConsumeEffect();
    public static final MapCodec<AmethysmConsumeEffect> CODEC = MapCodec.unit(INSTANCE);
    public static final PacketCodec<RegistryByteBuf, AmethysmConsumeEffect> PACKET_CODEC = PacketCodec.unit(INSTANCE);

    @Override
    public Type<? extends ConsumeEffect> getType() {
        return AMETHYSM_CONSUME_EFFECT_TYPE;
    }

    @Override
    public boolean onConsume(World world, ItemStack stack, LivingEntity entity) {
        if (stack.getEnchantments().getEnchantments().stream()
            .anyMatch(entry -> entry.getKey()
                .map(key -> key.equals(CustomEnchantments.CHARGE))
                .orElse(false))) {
            if (entity instanceof ServerPlayerEntity player) {
                LivesUtils.setLives(player, o -> o + 2);
                ServerPlayNetworking.send(player, new StartWhitenessScreenEvent(TICKS_PER_SECOND));
            }

            ((LivingEntityCustomInterface) entity).nine_lifes$setDamageTicks(TICKS_PER_SECOND);
            return true;
        }

        if (entity instanceof PlayerEntity player) {
            player.sendMessage(Text.literal("- .... .  .--. --- .-- . .-.  --- ..-.  .- -- . - .... -.-- ... -  .. ...  - --- ---  ... - .-. --- -. --.").formatted(Formatting.LIGHT_PURPLE), true);
        }

        return entity.addStatusEffect(new StatusEffectInstance(CustomEffects.AMETHYSM, 5 * TICKS_PER_SECOND, 0));
    }
}
