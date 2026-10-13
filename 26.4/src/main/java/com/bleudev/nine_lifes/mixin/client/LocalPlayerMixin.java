package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.api.render.client.PostEffectRegistry;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import kotlin.Unit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    @ModifyReturnValue(method = "getActivePostEffects", at = @At("RETURN"))
    private List<Identifier> addCustomPostEffects(List<Identifier> original) {
        ArrayList<Identifier> var10001 = new ArrayList<>(original);
        PostEffectRegistry.Companion.visit$com_bleudev_nine_lifes(var10002 -> {
            var10001.add(var10002);
            return Unit.INSTANCE;
        });
        return var10001;
    }
}
