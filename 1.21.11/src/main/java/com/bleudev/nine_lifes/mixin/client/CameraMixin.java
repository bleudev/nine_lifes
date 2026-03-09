package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.NineLifesClientData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Camera.class)
abstract public class CameraMixin {
    @Shadow
    protected abstract void setRotation(float yRot, float xRot);

    @Shadow
    private float yRot;
    @Shadow
    private float xRot;

    @Inject(method = "setup", at = @At("TAIL"))
    private void applyShake(Level level, Entity entity, boolean bl, boolean bl2, float f, CallbackInfo ci) {
        float strength = NineLifesClientData.INSTANCE.getShakeStrength();
        float speed = NineLifesClientData.INSTANCE.getShakeSpeed();
        if (strength > 0.0F) {
            float yaw = (float) (Math.sin(Util.getMillis() / 30.0 * speed) * strength);
            float pitch = (float) (Math.cos(Util.getMillis() / 60.0 * speed) * strength);
            this.setRotation((float) (this.yRot - pitch * Math.sqrt(2)), this.xRot + (yaw));
        }
    }
}
