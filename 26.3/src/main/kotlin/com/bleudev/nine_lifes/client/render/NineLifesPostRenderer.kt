package com.bleudev.nine_lifes.client.render

import com.bleudev.nine_lifes.api.render.client.PostEffectRegistry
import com.bleudev.nine_lifes.client.util.applyPostEffect
import com.mojang.blaze3d.resource.CrossFrameResourcePool
import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier

class NineLifesPostRenderer(val resourcePool: CrossFrameResourcePool) {
    init {
        PostEffectRegistry.initBuffers()
    }

    fun render() = PostEffectRegistry.execute(::renderPostEffect)

    private fun renderPostEffect(postEffectId: Identifier) = Minecraft.getInstance().applyPostEffect(postEffectId, resourcePool)
}