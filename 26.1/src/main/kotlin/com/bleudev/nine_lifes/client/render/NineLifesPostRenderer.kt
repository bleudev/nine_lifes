package com.bleudev.nine_lifes.client.render

import com.bleudev.nine_lifes.client.api.render.DynamicUniformsRegistryImpl
import com.bleudev.nine_lifes.client.util.applyPostEffect
import com.bleudev.nine_lifes.util.createIdentifier
import com.mojang.blaze3d.resource.CrossFrameResourcePool
import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier

class NineLifesPostRenderer(val resourcePool: CrossFrameResourcePool, val minecraftSupplier: () -> Minecraft) {
    init {
        DynamicUniformsRegistryImpl.initBuffers()
    }

    fun render() {
        DynamicUniformsRegistryImpl.updateBuffers()
        renderCustomPostEffect("redmaj")
        renderCustomPostEffect("anaglyph")
        renderCustomPostEffect("cblur")
    }

    private fun renderCustomPostEffect(name: String) = renderPostEffect(createIdentifier(name))
    private fun renderPostEffect(postEffectId: Identifier) = minecraftSupplier().applyPostEffect(postEffectId, resourcePool)
}