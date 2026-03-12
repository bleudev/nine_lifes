package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.api.render.client.DynamicUniformsRegistryImpl;
import com.mojang.blaze3d.buffers.GpuBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

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

    @Redirect(method = "method_67884", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/PostPass;customUniforms:Ljava/util/Map;", opcode = Opcodes.GETFIELD))
    private Map<String, GpuBuffer> modifyUniforms(PostPass instance) {
        return DynamicUniformsRegistryImpl.getNewUniforms$nine_lifes(this.customUniforms, Identifier.parse(this.name.split("/")[0]));
    }
}
