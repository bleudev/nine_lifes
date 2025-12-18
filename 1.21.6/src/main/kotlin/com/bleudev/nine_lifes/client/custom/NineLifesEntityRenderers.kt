package com.bleudev.nine_lifes.client.custom

import com.bleudev.nine_lifes.client.custom.entity.render.WanderingArmorStandRenderer
import com.bleudev.nine_lifes.custom.NineLifesEntities
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry

object NineLifesEntityRenderers {
    fun initialize() {
        EntityRendererRegistry.register(NineLifesEntities.WANDERING_ARMOR_STAND, ::WanderingArmorStandRenderer)
    }
}