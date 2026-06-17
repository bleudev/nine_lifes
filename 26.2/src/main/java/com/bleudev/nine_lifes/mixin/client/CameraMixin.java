package com.bleudev.nine_lifes.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nine_lifes.client.NineLifesClientStorageKt.*;

@Environment(EnvType.CLIENT)
@Mixin(Camera.class)
abstract public class CameraMixin {
    @Shadow
    protected abstract void setRotation(float yRot, float xRot);

    @Shadow
    private float yRot;
    @Shadow
    private float xRot;

    @Shadow
    protected abstract void setPosition(Vec3 vec3);

    @Shadow
    public abstract Vec3 position();

    @Shadow
    public abstract Quaternionf rotation();

    @Inject(method = "update", at = @At("TAIL"))
    private void applyShake(DeltaTracker deltaTracker, CallbackInfo ci) {
        float strength = getShakeStrength();
        float speed = getShakeSpeed();
        float xOffset = getXOffset();
        if (strength > 0.0F) {
            float dxrot = (float) (Math.sin(Util.getMillis() / 30.0 * speed) * strength);
            float dyrot = (float) (Math.cos(Util.getMillis() / 60.0 * speed) * strength);
            this.setRotation((float) (this.yRot - dyrot * Math.sqrt(2)), this.xRot + (dxrot));
        }
        if (xOffset != 0) {
            Vector3f off = new Vector3f(xOffset, 0, 0).rotate(this.rotation());
            this.setPosition(this.position().add(off.x, off.y, off.z));
        }
    }
}
