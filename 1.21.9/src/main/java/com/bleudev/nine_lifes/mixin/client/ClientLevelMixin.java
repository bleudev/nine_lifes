package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.client.FogProperitesKt;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Inject(method = "getSkyColor", at = @At("RETURN"), cancellable = true)
    private void modifySkyColor(Vec3 vec3, float f, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(FogProperitesKt.getSkyColor().invoke(cir.getReturnValue()));
    }
}
