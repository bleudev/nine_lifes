package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.api.render.client.impl.PostEffectRegistryImpl;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(PostPass.class)
public class PostPassMixin {
    @Shadow
    @Final
    private Map<String, GpuBuffer> customUniforms;

    @Shadow
    @Final
    private String name;

    @Unique
    private Identifier nameId;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setNameId(RenderPipeline pipeline, Identifier outputTargetId, Map<?, ?> uniformGroups, List<?> inputs, CallbackInfo ci) {
        nameId = Identifier.parse(this.name.split("/")[0]);
    }

    @Redirect(method = "lambda$addToFrame$1", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/PostPass;customUniforms:Ljava/util/Map;", opcode = Opcodes.GETFIELD))
    private Map<String, GpuBuffer> modifyUniforms(PostPass instance) {
        if (PostEffectRegistryImpl.shouldApplyBuffers(nameId)) {
            Map<String, GpuBuffer> overrides = PostEffectRegistryImpl.extractBuffers(nameId);
            if (!overrides.isEmpty()) {
                HashMap<String, GpuBuffer> result = new HashMap<>(this.customUniforms);
                result.putAll(overrides);
                return result;
            }
        }
        return this.customUniforms;
    }
}
