package com.bleudev.nine_lifes.client.custom

import com.bleudev.nine_lifes.client.custom.renderer.entity.WanderingArmorStandRenderer
import com.bleudev.nine_lifes.custom.NLEntityTypes
import net.minecraft.client.renderer.entity.EntityRenderers

object NLEntityRenderers {
    fun initialize() {
        EntityRenderers.register(NLEntityTypes.WANDERING_ARMOR_STAND, ::WanderingArmorStandRenderer)
    }
}