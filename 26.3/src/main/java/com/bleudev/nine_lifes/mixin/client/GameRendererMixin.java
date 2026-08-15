package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.client.render.NineLifesPostRenderer;
import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.resources.model.ModelManager;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Final
    @Shadow
    private CrossFrameResourcePool resourcePool;

    @Final
    @Unique
    @Mutable
    private NineLifesPostRenderer nineLifesPostRenderer;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initCustomShadersRenderer(Minecraft minecraft, FirstPersonHandsAndItemsRenderer firstPersonHandsAndItemsRenderer, ModelManager modelManager, ItemModelResolver itemModelResolver, CallbackInfo ci) {
        nineLifesPostRenderer = new NineLifesPostRenderer(resourcePool);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;blitEntityOutline()V", shift = At.Shift.AFTER))
    private void renderAdditionalShader(CallbackInfo ci) {
        nineLifesPostRenderer.render();
    }
}
