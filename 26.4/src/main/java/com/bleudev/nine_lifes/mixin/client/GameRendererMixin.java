package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.api.render.client.SpectatePostEffectRegistry;
import com.bleudev.nine_lifes.client.render.GlowRenderer;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    protected abstract void setSpectatedEntityPostEffect(Identifier id);

    @WrapMethod(method = "checkEntityPostEffect")
    private void additionalEntityPostEffects(@Nullable Entity cameraEntity, Operation<Void> original) {
        Identifier effect = SpectatePostEffectRegistry.Companion.visit$com_bleudev_nine_lifes(cameraEntity);
        if (effect != null) {
            setSpectatedEntityPostEffect(effect);
        } else {
            original.call(cameraEntity);
        }
    }

    @Inject(method = "close", at = @At("RETURN"))
    private void onClose(CallbackInfo ci) {
        GlowRenderer.reset();
    }
}
