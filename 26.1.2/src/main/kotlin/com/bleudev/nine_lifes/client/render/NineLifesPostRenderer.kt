package com.bleudev.nine_lifes.client.render

import com.bleudev.nine_lifes.api.render.client.DynamicUniformsRegistryImpl
import com.bleudev.nine_lifes.api.render.client.PostEffectRegistryImpl
import com.bleudev.nine_lifes.client.util.applyPostEffect
import com.mojang.blaze3d.resource.CrossFrameResourcePool
import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier

class NineLifesPostRenderer(val resourcePool: CrossFrameResourcePool, val minecraftSupplier: () -> Minecraft) {
    init {
        DynamicUniformsRegistryImpl.initBuffers()
    }

    fun render() {
        DynamicUniformsRegistryImpl.updateBuffers()
        PostEffectRegistryImpl.execute(::renderPostEffect)
    }

    private fun renderPostEffect(postEffectId: Identifier) = minecraftSupplier().applyPostEffect(postEffectId, resourcePool)
}