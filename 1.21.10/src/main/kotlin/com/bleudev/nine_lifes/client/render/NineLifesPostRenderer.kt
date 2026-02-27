package com.bleudev.nine_lifes.client.render

import com.bleudev.nine_lifes.client.api.render.DynamicUniformsRegistryImpl
import com.bleudev.nine_lifes.util.createIdentifier
import com.mojang.blaze3d.framegraph.FrameGraphBuilder
import com.mojang.blaze3d.resource.CrossFrameResourcePool
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.LevelTargetBundle
import net.minecraft.client.renderer.PostChain
import net.minecraft.resources.ResourceLocation

class NineLifesPostRenderer(val resourcePool: CrossFrameResourcePool, val minecraftSupplier: () -> Minecraft) {
    init {
        DynamicUniformsRegistryImpl.initBuffers()
    }

    fun render() {
        DynamicUniformsRegistryImpl.updateBuffers()
        renderCustomPostEffect("redmaj")
    }

    private fun renderCustomPostEffect(name: String) = renderPostEffect(createIdentifier(name))
    private fun renderPostEffect(postEffectId: ResourceLocation) {
        val minecraft = minecraftSupplier()
        val postChain = minecraft.shaderManager.getPostChain(postEffectId, LevelTargetBundle.MAIN_TARGETS)
        if (postChain != null) {
            val mainTarget = minecraft.mainRenderTarget
            val frame = FrameGraphBuilder()
            val targets = PostChain.TargetBundle.of(PostChain.MAIN_TARGET_ID, frame.importExternal("main", mainTarget))
            postChain.addToFrame(frame, mainTarget.width, mainTarget.height, targets)
            frame.execute(resourcePool)
        }
    }
}