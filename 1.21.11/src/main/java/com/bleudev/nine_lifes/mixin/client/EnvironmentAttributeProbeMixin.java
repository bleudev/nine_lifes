package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.api.event.client.ClientEnvironmentSetupEvents;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
import net.minecraft.world.attribute.EnvironmentAttributes;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unchecked")
@Mixin(EnvironmentAttributeProbe.class)
public class EnvironmentAttributeProbeMixin {
    @Inject(method = "getValue", at = @At("RETURN"), cancellable = true)
    private <Value> void modifySkyColor(EnvironmentAttribute<@NotNull Value> environmentAttribute, float f, CallbackInfoReturnable<Value> cir) {
        if (environmentAttribute.equals(EnvironmentAttributes.SKY_COLOR)) {
            int original = (Integer) cir.getReturnValue();
            cir.setReturnValue((Value) ClientEnvironmentSetupEvents.SKY_COLOR.invoker().invoke(original, original));
        }
    }
}
