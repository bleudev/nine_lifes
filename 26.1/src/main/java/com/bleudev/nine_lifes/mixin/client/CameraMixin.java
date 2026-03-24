package com.bleudev.nine_lifes.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nine_lifes.client.NineLifesClientStorageKt.getShakeSpeed;
import static com.bleudev.nine_lifes.client.NineLifesClientStorageKt.getShakeStrength;

@Environment(EnvType.CLIENT)
@Mixin(Camera.class)
abstract public class CameraMixin {
    @Shadow
    protected abstract void setRotation(float yRot, float xRot);

    @Shadow
    private float yRot;
    @Shadow
    private float xRot;

    @Inject(method = "update", at = @At("TAIL"))
    private void applyShake(DeltaTracker deltaTracker, CallbackInfo ci) {
        float strength = getShakeStrength();
        float speed = getShakeSpeed();
        if (strength > 0.0F) {
            float yaw = (float) (Math.sin(Util.getMillis() / 30.0 * speed) * strength);
            float pitch = (float) (Math.cos(Util.getMillis() / 60.0 * speed) * strength);
            this.setRotation((float) (this.yRot - pitch * Math.sqrt(2)), this.xRot + (yaw));
        }
    }
}
