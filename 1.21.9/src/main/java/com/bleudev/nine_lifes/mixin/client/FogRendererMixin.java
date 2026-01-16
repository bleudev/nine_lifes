package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.client.FogProperitesKt;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Inject(method = "computeFogColor", at = @At("RETURN"), cancellable = true)
    private void modifyFogColor(Camera camera, float f, ClientLevel clientLevel, int i, float g, boolean bl, CallbackInfoReturnable<Vector4f> cir) {
        cir.setReturnValue(FogProperitesKt.getFogColor().invoke(cir.getReturnValue()));
    }

    @WrapOperation(method = "setupFog", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/fog/environment/FogEnvironment;setupFog(Lnet/minecraft/client/renderer/fog/FogData;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/BlockPos;Lnet/minecraft/client/multiplayer/ClientLevel;FLnet/minecraft/client/DeltaTracker;)V"))
    private void modifyFogProperties(FogEnvironment instance, FogData fogData, Entity entity, BlockPos blockPos, ClientLevel clientLevel, float v, DeltaTracker deltaTracker, Operation<Void> original) {
        original.call(instance, fogData, entity, blockPos, clientLevel, v, deltaTracker);
        fogData.environmentalStart = FogProperitesKt.getFogStart().invoke(fogData.environmentalStart);
        fogData.renderDistanceStart = FogProperitesKt.getFogStart().invoke(fogData.renderDistanceStart);
        fogData.environmentalEnd = FogProperitesKt.getFogEnd().invoke(fogData.environmentalEnd);
        fogData.renderDistanceEnd = FogProperitesKt.getFogEnd().invoke(fogData.renderDistanceEnd);
    }
}
