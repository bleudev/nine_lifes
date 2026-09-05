package com.bleudev.nine_lifes.client.custom.entity.render

import com.bleudev.nine_lifes.client.custom.entity.model.WanderingArmorStandModel
import com.bleudev.nine_lifes.custom.entity.WanderingArmorStand
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.model.`object`.armorstand.ArmorStandArmorModel
import net.minecraft.client.renderer.entity.*
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer
import net.minecraft.client.renderer.entity.layers.WingsLayer
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState
import net.minecraft.resources.Identifier
import net.minecraft.util.Mth

class WanderingArmorStandRenderer(ctx: EntityRendererProvider.Context) : LivingEntityRenderer<WanderingArmorStand, ArmorStandRenderState, WanderingArmorStandModel>(
    ctx, WanderingArmorStandModel(ctx.bakeLayer(ModelLayers.ARMOR_STAND)), 0f
) {
    init {
        this.addLayer(HumanoidArmorLayer(this, ArmorModelSet.bake(ModelLayers.ARMOR_STAND_ARMOR, ctx.modelSet, ::ArmorStandArmorModel), ctx.equipmentRenderer))
        this.addLayer(WingsLayer(this, ctx.modelSet, ctx.equipmentRenderer))
        this.addLayer(CustomHeadLayer(this, ctx.modelSet, ctx.playerSkinRenderCache))
    }
    override fun getTextureLocation(state: ArmorStandRenderState): Identifier = ArmorStandRenderer.DEFAULT_SKIN_LOCATION
    override fun createRenderState(): ArmorStandRenderState = ArmorStandRenderState()

    protected override fun shouldShowName(entity: WanderingArmorStand, d: Double): Boolean {
        return entity.hasCustomName() && super.shouldShowName(entity, d)
    }

    override fun extractRenderState(entity: WanderingArmorStand, state: ArmorStandRenderState, partialTicks: Float) {
        super.extractRenderState(entity, state, partialTicks)
        HumanoidMobRenderer.extractHumanoidRenderState(entity, state, partialTicks, itemModelResolver)
        state.showArms = false // Real armor stands don't have arms, do they?
        if (entity.isAlive) {
            state.walkAnimationPos = entity.walkAnimation.position(partialTicks)
            state.walkAnimationSpeed = entity.walkAnimation.speed(partialTicks)
        } else {
            state.walkAnimationSpeed = 0f
            state.walkAnimationPos = 0f
        }
        state.wiggle = (entity.level().gameTime - entity.lastHit).toFloat() + partialTicks

    }
    override fun setupRotations(
        state: ArmorStandRenderState,
        poseStack: PoseStack,
        bodyRot: Float,
        entityScale: Float
    ) {
        super.setupRotations(state, poseStack, bodyRot, entityScale)
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - bodyRot))
        if (state.wiggle < 5.0f) {
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin((state.wiggle / 1.5f * Math.PI.toFloat()).toDouble()) * 3.0f))
        }
    }
}