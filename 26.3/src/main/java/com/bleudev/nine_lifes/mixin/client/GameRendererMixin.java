package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.api.render.client.impl.PostEffectRegistryImpl;
import com.bleudev.nine_lifes.client.render.GlowRenderer;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @WrapMethod(method = "close")
    private void onClose(Operation<Void> original) {
        original.call();
        GlowRenderer.close();
        PostEffectRegistryImpl.close();
    }
}
