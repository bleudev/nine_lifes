package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.api.event.client.ClientEnvironmentSetupEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
import net.minecraft.world.attribute.EnvironmentAttributes;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.bleudev.nine_lifes.client.NineLifesClientStorageKt.getForceAlwaysDay;

@SuppressWarnings("unchecked")
@Environment(EnvType.CLIENT)
@Mixin(EnvironmentAttributeProbe.class)
public class EnvironmentAttributeProbeMixin {
    @Inject(method = "getValue", at = @At("RETURN"), cancellable = true)
    private <Value> void modifySkyColor(EnvironmentAttribute<@NotNull Value> attribute, float partialTicks, CallbackInfoReturnable<Value> cir) {
        if (attribute.equals(EnvironmentAttributes.SKY_COLOR)) {
            int original = (Integer) cir.getReturnValue();
            cir.setReturnValue((Value) ClientEnvironmentSetupEvents.SKY_COLOR.invoker().invoke(original, original));
        }
        // Always day
        if (getForceAlwaysDay()) {
            if (attribute.equals(EnvironmentAttributes.SKY_LIGHT_FACTOR))
                cir.setReturnValue((Value) (Float) 1f);
            if (attribute.equals(EnvironmentAttributes.SKY_LIGHT_COLOR)) {
                cir.setReturnValue((Value) (Integer) (-1));
            }
            if (attribute.equals(EnvironmentAttributes.SUNRISE_SUNSET_COLOR)) {
                cir.setReturnValue((Value) (Integer) 0);
            }
            if (attribute.equals(EnvironmentAttributes.SUN_ANGLE)) {
                cir.setReturnValue((Value) (Float) 0f);
            }
            if (attribute.equals(EnvironmentAttributes.MOON_ANGLE)) {
                cir.setReturnValue((Value) (Float) 180f);
            }
            if (attribute.equals(EnvironmentAttributes.STAR_BRIGHTNESS)) {
                cir.setReturnValue((Value) (Float) 0f);
            }
            if (attribute.equals(EnvironmentAttributes.CLOUD_COLOR)) {
                cir.setReturnValue((Value) (Integer) (-855638017));
            }
        }
    }
}
