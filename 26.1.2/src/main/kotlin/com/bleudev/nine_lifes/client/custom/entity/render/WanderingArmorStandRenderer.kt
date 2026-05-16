package com.bleudev.nine_lifes.client.custom.entity.render

import com.bleudev.nine_lifes.client.custom.entity.model.WanderingArmorStandModel
import com.bleudev.nine_lifes.custom.entity.WanderingArmorStand
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.entity.ArmorStandRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState
import net.minecraft.resources.Identifier

class WanderingArmorStandRenderer(ctx: EntityRendererProvider.Context) : LivingEntityRenderer<WanderingArmorStand, ArmorStandRenderState, WanderingArmorStandModel>(
    ctx, WanderingArmorStandModel(ctx.bakeLayer(ModelLayers.ARMOR_STAND)), 0f) {
    override fun getTextureLocation(state: ArmorStandRenderState): Identifier = ArmorStandRenderer.DEFAULT_SKIN_LOCATION
    override fun createRenderState(): ArmorStandRenderState = ArmorStandRenderState()

    protected override fun shouldShowName(entity: WanderingArmorStand, d: Double): Boolean {
        if (entity.hasCustomName()) return super.shouldShowName(entity, d)
        return false
    }

    override fun extractRenderState(entity: WanderingArmorStand, state: ArmorStandRenderState, f: Float) {
        super.extractRenderState(entity, state, f)
        state.showArms = true
        if (entity.isAlive) {
            state.walkAnimationPos = entity.walkAnimation.position(f)
            state.walkAnimationSpeed = entity.walkAnimation.speed(f)
        } else {
            state.walkAnimationSpeed = 0f
            state.walkAnimationPos = 0f
        }
    }
}