package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.api.render.GlowState;
import com.bleudev.nine_lifes.client.render.GlowRenderer;
import com.bleudev.nine_lifes.interfaces.mixin.ILivingEntityRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

import static com.bleudev.nine_lifes.client.NineLifesClientStorageKt.getEntityGlowEffects;

@SuppressWarnings("RedundantCast") // Idk why classtweaker doesn't work
@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState> {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("RETURN"))
    private void extractId(T entity, S state, float partialTicks, CallbackInfo ci) {
        ((ILivingEntityRenderState) state).setEntityId(entity.getUUID());
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("RETURN"))
    private void chargedGlow(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        UUID id;
        GlowState glowState;
        if ((id = ((ILivingEntityRenderState) state).entityId()) != null && (glowState = getEntityGlowEffects().get(id)) != null) {
            Vec3 position = new Vec3(state.x, state.y + state.boundingBoxHeight / 2, state.z);
            GlowRenderer.getInstance().submitAt(submitNodeCollector, camera, position, glowState);
        }
    }
}
