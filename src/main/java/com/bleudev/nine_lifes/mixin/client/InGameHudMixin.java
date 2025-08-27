package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.util.LivesUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Unique
    private void drawCenterHeart(DrawContext context, Identifier texture, int lives) {
        int x0 = context.getScaledWindowWidth() / 2;
        int y = context.getScaledWindowHeight() - 48;

        float delta = (float) lives / 9;

        context.drawTexture(
            RenderPipelines.GUI_TEXTURED, texture,
            x0 - 9, y - 5, 0, 0, 18, 18, 18, 18,
            ColorHelper.fromFloats(0.75f, delta, delta, delta)
        );
    }

    @Inject(method = "renderStatusBars", at = @At("RETURN"), cancellable = true)
    private void renderCounter(DrawContext context, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) ci.cancel();

        TextRenderer textRenderer = client.textRenderer;

        int lives = 9;
        if (client.getServer() != null)
            lives = LivesUtils.getLives(client.getServer().getPlayerManager().getPlayer(client.player.getUuid()));

        Text text = Text.literal("" + lives);

        int x0 = context.getScaledWindowWidth() / 2;
        int x = x0 - textRenderer.getWidth(text) / 2;
        int y = context.getScaledWindowHeight() - 48;

//        if (client.player.experienceLevel > 0) {
//            y -= 6;
//        }

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(0, 0);
        drawCenterHeart(context, Identifier.ofVanilla("textures/gui/sprites/hud/heart/container_hardcore.png"), lives);
        drawCenterHeart(context, Identifier.ofVanilla("textures/gui/sprites/hud/heart/hardcore_full.png"), lives);
        context.drawTextWithShadow(textRenderer, text, x, y, 0xffffffff);
        context.getMatrices().popMatrix();
    }

}

