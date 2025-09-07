package com.bleudev.nine_lifes.custom.consume;

import com.bleudev.nine_lifes.custom.CustomEffects;
import com.mojang.serialization.MapCodec;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
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
    public boolean onConsume(World world, ItemStack stack, LivingEntity user) {
        if (user instanceof PlayerEntity player) {
            player.sendMessage(Text.literal("- .... .  .--. --- .-- . .-.  --- ..-.  .- -- . - .... -.-- ... -  .. ...  - --- ---  ... - .-. --- -. --.").formatted(Formatting.LIGHT_PURPLE), true);
        }
        return user.addStatusEffect(new StatusEffectInstance(CustomEffects.AMETHYSM, 5 * TICKS_PER_SECOND, 0));
    }
}
