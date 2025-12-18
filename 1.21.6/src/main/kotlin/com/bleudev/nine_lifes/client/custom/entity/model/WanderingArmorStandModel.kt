package com.bleudev.nine_lifes.client.custom.entity.model

import net.minecraft.client.model.ArmorStandModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState
import kotlin.math.cos

class WanderingArmorStandModel(modelPart: ModelPart) : ArmorStandModel(modelPart) {
    override fun setupAnim(state: ArmorStandRenderState) {
        super.setupAnim(state)

        val f = state.walkAnimationPos
        val g = state.walkAnimationSpeed
        this.rightLeg.xRot = cos(f * 0.6662f) * 1.4f * g
        this.leftLeg.xRot = cos(f * 0.6662f + Math.PI.toFloat()) * 1.4f * g
        this.rightArm.xRot = cos(f * 0.6662f + Math.PI.toFloat()) * 1.4f * g
        this.leftArm.xRot = cos(f * 0.6662f) * 1.4f * g
    }
}