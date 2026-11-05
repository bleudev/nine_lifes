package com.bleudev.nine_lifes.client.custom

import com.bleudev.nine_lifes.client.custom.renderer.entity.WanderingArmorStandRenderer
import com.bleudev.nine_lifes.custom.NLEntityTypes
import net.minecraft.client.core.registries.BuiltInClientRegistries
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

object NLEntityRenderers {
    fun initialize() {
        register(NLEntityTypes.WANDERING_ARMOR_STAND, ::WanderingArmorStandRenderer)
    }

    private fun <T : Entity> register(type: EntityType<T>, provider: EntityRendererProvider<T>) {
        EntityRenderers.register(BuiltInClientRegistries.ENTITY_RENDERER, type, provider)
    }
}