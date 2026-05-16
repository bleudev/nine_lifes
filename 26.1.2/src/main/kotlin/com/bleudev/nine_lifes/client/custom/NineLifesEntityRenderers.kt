package com.bleudev.nine_lifes.client.custom

import com.bleudev.nine_lifes.client.custom.entity.render.WanderingArmorStandRenderer
import com.bleudev.nine_lifes.custom.NineLifesEntities
import net.minecraft.client.renderer.entity.EntityRenderers

object NineLifesEntityRenderers {
    fun initialize() {
        EntityRenderers.register(NineLifesEntities.WANDERING_ARMOR_STAND, ::WanderingArmorStandRenderer)
    }
}