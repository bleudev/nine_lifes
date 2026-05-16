package com.bleudev.nine_lifes.client.util

import com.bleudev.nine_lifes.util.createIdentifier
import com.mojang.blaze3d.framegraph.FrameGraphBuilder
import com.mojang.blaze3d.resource.CrossFrameResourcePool
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.LevelTargetBundle
import net.minecraft.client.renderer.PostChain
import net.minecraft.resources.Identifier
import net.minecraft.util.ARGB

private val RESOURCE_POOL = CrossFrameResourcePool(3)

fun GuiGraphicsExtractor.overlayWithColor(color: Int) = fill(0, 0, guiWidth(), guiHeight(), color)
fun GuiGraphicsExtractor.overlayWithColor(alpha: Float, red: Float, green: Float, blue: Float) = overlayWithColor(ARGB.colorFromFloat(alpha, red, green, blue))
fun GuiGraphicsExtractor.white(alpha: Float) = overlayWithColor(0xffffff.asColorWithAlpha(alpha))
fun GuiGraphicsExtractor.white() = white(1f)

fun Minecraft.applyPostEffect(id: Identifier, pool: CrossFrameResourcePool) {
    val postChain = shaderManager.getPostChain(id, LevelTargetBundle.MAIN_TARGETS)
    if (postChain != null) {
        val mainTarget = mainRenderTarget
        val frame = FrameGraphBuilder()
        val targets = PostChain.TargetBundle.of(PostChain.MAIN_TARGET_ID, frame.importExternal("main", mainTarget))
        postChain.addToFrame(frame, mainTarget.width, mainTarget.height, targets)
        frame.execute(pool)
    }
}
fun Minecraft.applyAnaglyph() = applyPostEffect(createIdentifier("anaglyph"), RESOURCE_POOL)

// Color
fun Int.asColorWithAlpha(alpha: Float) =
    ARGB.colorFromFloat(alpha, ARGB.redFloat(this), ARGB.greenFloat(this), ARGB.blueFloat(this))
fun Int.asColorWithAlpha(alpha: Double) = asColorWithAlpha(alpha.toFloat())