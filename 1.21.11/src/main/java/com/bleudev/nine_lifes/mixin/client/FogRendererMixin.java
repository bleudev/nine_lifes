package com.bleudev.nine_lifes.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Inject(method = "computeFogColor", at = @At("RETURN"), cancellable = true)
    private void modifyFogColor(Camera camera, float f, ClientLevel clientLevel, int i, float g, CallbackInfoReturnable<Vector4f> cir) {
        cir.setReturnValue(new Vector4f(1f, 0f, 0f, 1f));
    }
}
