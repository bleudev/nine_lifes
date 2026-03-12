package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.api.event.client.ClientEnvironmentSetupEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Inject(method = "computeFogColor", at = @At("RETURN"), cancellable = true)
    private void modifyFogColor(Camera camera, float f, ClientLevel clientLevel, int i, float g, CallbackInfoReturnable<Vector4f> cir) {
        Vector4f orig = cir.getReturnValue();
        cir.setReturnValue(ClientEnvironmentSetupEvents.FOG_COLOR.invoker().invoke(orig, orig));
    }

    @WrapOperation(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/fog/environment/FogEnvironment;setupFog(Lnet/minecraft/client/renderer/fog/FogData;Lnet/minecraft/client/Camera;Lnet/minecraft/client/multiplayer/ClientLevel;FLnet/minecraft/client/DeltaTracker;)V"))
    private void modifyFogProperties(FogEnvironment instance, FogData fogData, Camera camera, ClientLevel clientLevel, float v, DeltaTracker deltaTracker, Operation<Void> original) {
        original.call(instance, fogData, camera, clientLevel, v, deltaTracker);

        Function<Float, Float> start = (value) -> ClientEnvironmentSetupEvents.FOG_START.invoker().invoke(value, value);
        Function<Float, Float> end = (value) -> ClientEnvironmentSetupEvents.FOG_END.invoker().invoke(value, value);
        fogData.environmentalStart = start.apply(fogData.environmentalStart);
        fogData.renderDistanceStart = start.apply(fogData.renderDistanceStart);
        fogData.environmentalEnd = end.apply(fogData.environmentalEnd);
        fogData.renderDistanceEnd = end.apply(fogData.renderDistanceEnd);
    }
}
