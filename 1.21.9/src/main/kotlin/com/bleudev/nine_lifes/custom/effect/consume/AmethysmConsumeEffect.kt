package com.bleudev.nine_lifes.custom.effect.consume

import com.bleudev.nine_lifes.custom.NineLifesConsumeEffects
import com.bleudev.nine_lifes.custom.NineLifesEnchantments
import com.bleudev.nine_lifes.custom.NineLifesMobEffects
import com.bleudev.nine_lifes.custom.packet.payload.StartWhitenessScreen
import com.bleudev.nine_lifes.interfaces.mixin.CustomLivingEntity
import com.bleudev.nine_lifes.util.sendPacket
import com.bleudev.nine_lifes.util.setLifes
import com.mojang.serialization.MapCodec
import net.minecraft.ChatFormatting
import net.minecraft.SharedConstants.TICKS_PER_SECOND
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.StreamCodec
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
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
        if (itemStack.enchantments.keySet().any { it.`is`(NineLifesEnchantments.KEY_CHARGE) }) {
            if (livingEntity is ServerPlayer) {
                livingEntity.setLifes { o -> o + 2 }
                livingEntity.sendPacket(StartWhitenessScreen(TICKS_PER_SECOND, 1f))
            }
            (livingEntity as CustomLivingEntity).`nl$setDamageTicks`(TICKS_PER_SECOND)
            return true
        }

        if (livingEntity is Player) livingEntity.displayClientMessage(Component.literal(
                "- .... .  .--. --- .-- . .-.  --- ..-.  .- -- . - .... -.-- ... -  .. ...  - --- ---  ... - .-. --- -. --."
            ).withStyle(ChatFormatting.LIGHT_PURPLE), true)

        return livingEntity.addEffect(MobEffectInstance(NineLifesMobEffects.AMETHYSM, 100, 0))
    }
}