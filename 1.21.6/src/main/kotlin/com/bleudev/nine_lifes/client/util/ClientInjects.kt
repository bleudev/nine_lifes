package com.bleudev.nine_lifes.client.util

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.util.ARGB

// Rendering
fun GuiGraphics.anaglyph(renderer: (Int) -> Unit, offsetX: Float, offsetY: Float = 0f, baseColor: Int = -0x1) {
    val a = ARGB.alphaFloat(baseColor)
    val r = ARGB.redFloat(baseColor)
    val g = ARGB.greenFloat(baseColor)
    val b = ARGB.blueFloat(baseColor)
    this.pose().pushMatrix()
    // Light blue side
    this.pose().translate(+offsetX, +offsetY)
    renderer(ARGB.colorFromFloat(a, 0f, g, b))
    // Red side
    this.pose().translate(-2 * offsetX, -2 * offsetY)
    renderer(ARGB.colorFromFloat(a, r, 0f, 0f))
    // Normal side
    this.pose().translate(+offsetX, +offsetY)
    renderer(baseColor)
    this.pose().popMatrix()
}
fun GuiGraphics.overlayWithColor(color: Int) = fill(0, 0, guiWidth(), guiHeight(), color)

// Color
fun Int.asColorWithAlpha(alpha: Float) =
    ARGB.colorFromFloat(alpha, ARGB.redFloat(this), ARGB.greenFloat(this), ARGB.blueFloat(this))
fun Int.asColorWithAlpha(alpha: Double) = asColorWithAlpha(alpha.toFloat())