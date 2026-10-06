package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.client.render.GlowRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Avatar;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nine_lifes.util.RegistryUtilsKt.createIdentifier;

@Environment(EnvType.CLIENT)
@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {
    @Unique
    @Final
    private static final Identifier ID_GLOW_CHARGED_AVATAR = createIdentifier("glow/charged/avatar");

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("RETURN"))
    private void extractGlow(Avatar entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        GlowRenderer.getInstance().getGlowMap().put(ID_GLOW_CHARGED_AVATAR, new GlowRenderer.Glow(entity.position().add(0.0, 1.0, 0.0), 1.5f, 1f, new Vector3f(1f, 1f, 1f), 32f, 64f));
    }
}
