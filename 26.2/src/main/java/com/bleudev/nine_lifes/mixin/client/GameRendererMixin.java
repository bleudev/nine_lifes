package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.api.render.client.SpectatePostEffectRegistry;
import com.bleudev.nine_lifes.client.render.GlowRenderer;
import com.bleudev.nine_lifes.client.render.NineLifesPostRenderer;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Final
    @Shadow
    private CrossFrameResourcePool resourcePool;

    @Shadow
    protected abstract void setPostEffect(Identifier id);

    @Final
    @Unique
    @Mutable
    private NineLifesPostRenderer nineLifesPostRenderer;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initCustomShadersRenderer(Minecraft minecraft, ItemInHandRenderer itemInHandRenderer, ModelManager modelManager, CallbackInfo ci) {
        nineLifesPostRenderer = new NineLifesPostRenderer(resourcePool);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = At.Shift.AFTER))
    private void renderAdditionalShader(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
        nineLifesPostRenderer.render();
    }

    @WrapMethod(method = "checkEntityPostEffect")
    private void additionalEntityPostEffects(Entity cameraEntity, Operation<Void> original) {
        Identifier effect = SpectatePostEffectRegistry.Companion.visit$com_bleudev_nine_lifes(cameraEntity);
        if (effect != null) {
            setPostEffect(effect);
        } else {
            original.call(cameraEntity);
        }
    }

    @Inject(method = "close", at = @At("RETURN"))
    private void onClose(CallbackInfo ci) {
        GlowRenderer.reset();
    }
}
