package com.bleudev.nine_lifes.custom.effect.mob

import com.bleudev.nine_lifes.custom.NineLifesDamageSources
import com.bleudev.nine_lifes.custom.NineLifesMobEffects
import com.bleudev.nine_lifes.custom.packet.payload.StartAmethysmScreen
import com.bleudev.nine_lifes.util.sendPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeMap

class AmethysmEffect : MobEffect(MobEffectCategory.NEUTRAL, 0xff00ff) {
    private var ticks = 0
    private var entity: LivingEntity? = null

    override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean = true

    override fun applyEffectTick(level: ServerLevel, entity: LivingEntity, amplifier: Int): Boolean {
        if (!level.isClientSide) {
            ticks++
            this.entity = entity
            if (!entity.isSleeping) {
                if (ticks % 20 == 0)
                    entity.hurtServer(level, NineLifesDamageSources.amethysm(level), 1f)
                entity.setDeltaMovement(.0, 0.05, .0)
                entity.needsSync = true

                if (entity is ServerPlayer)
                    entity.connection.send(ClientboundSetEntityMotionPacket(entity))
            }
            entity.setGlowingTag(true)
        }
        return true
    }

    override fun onEffectAdded(entity: LivingEntity, amplifier: Int) {
        super.onEffectAdded(entity, amplifier)
        ticks = 0
        this.entity = entity
        if (entity is ServerPlayer)
            entity.sendPacket(StartAmethysmScreen(entity.getEffect(NineLifesMobEffects.AMETHYSM)?.duration ?: return))
    }

    override fun removeAttributeModifiers(attributeMap: AttributeMap) {
        super.removeAttributeModifiers(attributeMap)
        (this.entity as LivingEntity).let {
            if (!it.hasEffect(MobEffects.GLOWING)) it.setGlowingTag(false)
        }
    }
}