package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.client.FogProperitesKt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
import net.minecraft.world.attribute.EnvironmentAttributes;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.bleudev.nine_lifes.NineLifesClientData.lifes;

@SuppressWarnings("unchecked")
@Environment(EnvType.CLIENT)
@Mixin(EnvironmentAttributeProbe.class)
public class EnvironmentAttributeProbeMixin {
    @Inject(method = "getValue", at = @At("RETURN"), cancellable = true)
    private <Value> void modifySkyColor(EnvironmentAttribute<@NotNull Value> environmentAttribute, float f, CallbackInfoReturnable<Value> cir) {
        if (environmentAttribute.equals(EnvironmentAttributes.SKY_COLOR)) {
            cir.setReturnValue((Value) FogProperitesKt.getSkyColor().invoke((Integer) cir.getReturnValue()));
        }
        // Always day
        var pl = Minecraft.getInstance().player;
        if (lifes <= 3 && pl != null) {
            var gm = pl.gameMode();
            if (gm != null && gm.isSurvival()) {
                if (environmentAttribute.equals(EnvironmentAttributes.SKY_LIGHT_FACTOR))
                    cir.setReturnValue((Value) Float.valueOf(1f));
                if (environmentAttribute.equals(EnvironmentAttributes.SKY_LIGHT_COLOR)) {
                    cir.setReturnValue((Value) Integer.valueOf(-1));
                }
                if (environmentAttribute.equals(EnvironmentAttributes.SUN_ANGLE)) {
                    cir.setReturnValue((Value) Float.valueOf(0f));
                }
                if (environmentAttribute.equals(EnvironmentAttributes.MOON_ANGLE)) {
                    cir.setReturnValue((Value) Float.valueOf(180f));
                }
                if (environmentAttribute.equals(EnvironmentAttributes.STAR_BRIGHTNESS)) {
                    cir.setReturnValue((Value) Float.valueOf(0f));
                }
                if (environmentAttribute.equals(EnvironmentAttributes.CLOUD_COLOR)) {
                    cir.setReturnValue((Value) Integer.valueOf(-855638017));
                }
            }
        }
    }
}
