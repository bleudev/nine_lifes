package com.bleudev.nine_lifes.custom.effect.consume

import com.bleudev.nine_lifes.CHARGED_EFFECT_DURATION
import com.bleudev.nine_lifes.custom.NineLifesConsumeEffects
import com.bleudev.nine_lifes.custom.NineLifesEnchantments
import com.bleudev.nine_lifes.custom.NineLifesMobEffects
import com.bleudev.nine_lifes.custom.packet.payload.StartWhitenessScreen
import com.bleudev.nine_lifes.util.damageTicks
import com.bleudev.nine_lifes.util.sendPacket
import com.mojang.serialization.MapCodec
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.consume_effects.ConsumeEffect
import net.minecraft.world.level.Level

class AmethysmConsumeEffect : ConsumeEffect {
    companion object {
        val INSTANCE: AmethysmConsumeEffect = AmethysmConsumeEffect()
        val MAP_CODEC: MapCodec<AmethysmConsumeEffect> = MapCodec.unit(INSTANCE)
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, AmethysmConsumeEffect> = StreamCodec.unit(INSTANCE)
    }

    override fun getType(): ConsumeEffect.Type<out ConsumeEffect> = NineLifesConsumeEffects.AMETHYSM

    override fun apply(level: Level, itemStack: ItemStack, livingEntity: LivingEntity): Boolean {
        if (itemStack.enchantments.keySet().any { it.`is`(NineLifesEnchantments.CHARGE) }) {
            if (livingEntity is ServerPlayer)
                livingEntity.sendPacket(StartWhitenessScreen(CHARGED_EFFECT_DURATION, 1f))
            livingEntity.damageTicks = CHARGED_EFFECT_DURATION
            return true
        }
        return livingEntity.addEffect(MobEffectInstance(NineLifesMobEffects.AMETHYSM, 100, 0))
    }
}