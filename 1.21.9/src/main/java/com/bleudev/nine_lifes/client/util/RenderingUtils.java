package com.bleudev.nine_lifes.client.util;

import net.minecraft.util.math.ColorHelper;
import org.joml.Matrix3x2fStack;

import java.util.function.Consumer;

public class RenderingUtils {
    public static void renderAnaglyph(Matrix3x2fStack matrix, Consumer<Integer> renderer, float offsetX, float offsetY, int base_color) {
        float a = ColorHelper.getAlphaFloat(base_color);
        float r = ColorHelper.getRedFloat(base_color);
        float g = ColorHelper.getGreenFloat(base_color);
        float b = ColorHelper.getBlueFloat(base_color);

        // Light blue side
        matrix.translate(+offsetX, +offsetY);
        renderer.accept(ColorHelper.fromFloats(a, 0, g, b));
        // Red side
        matrix.translate(-2*offsetX, -2*offsetY);
        renderer.accept(ColorHelper.fromFloats(a, r, 0, 0));
        // Normal side
        matrix.translate(+offsetX, +offsetY);
        renderer.accept(base_color);
    }
    public static void renderAnaglyph(Matrix3x2fStack matrix, Consumer<Integer> renderer, float offsetX, int base_color) {
        renderAnaglyph(matrix, renderer, offsetX, 0, base_color);
    }
    public static void renderAnaglyph(Matrix3x2fStack matrix, Consumer<Integer> renderer, float offsetX) {
        renderAnaglyph(matrix, renderer, offsetX, 0xffffffff);
    }
}
