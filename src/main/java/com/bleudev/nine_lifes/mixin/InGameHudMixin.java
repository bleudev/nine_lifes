package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.util.LivesUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "renderStatusBars", at = @At("RETURN"), cancellable = true)
    private void renderCounter(DrawContext context, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) ci.cancel();

        System.out.println("render!");

        TextRenderer textRenderer = client.textRenderer;

        int lives = 9;
        if (client.getServer() != null)
            lives = LivesUtils.getLives(client.getServer().getPlayerManager().getPlayer(client.player.getUuid()));

        Text text = Text.literal("" + lives);

        // координаты по умолчанию — по центру, над хотбаром
        int x = context.getScaledWindowWidth() / 2 - textRenderer.getWidth(text) / 2;
        int y = context.getScaledWindowHeight() - 48;

        // если есть уровень опыта — чуть выше, чтобы не наезжало
        if (client.player.experienceLevel > 0) {
            y -= 6;
        }

        context.getMatrices().pushMatrix();
        context.getMatrices().translate(0, 0); // поднять по Z, чтобы было поверх
        context.drawTextWithShadow(textRenderer, text, x, y, 0xFFFFFFff);
        context.getMatrices().popMatrix();
    }

}

