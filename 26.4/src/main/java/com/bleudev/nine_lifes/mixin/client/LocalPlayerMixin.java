package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.api.render.client.impl.PostEffectRegistryImpl;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
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
        List<Identifier> added = PostEffectRegistryImpl.extractPostEffects();

        if (added.isEmpty()) {
            return original;
        } else {
            ArrayList<Identifier> result = new ArrayList<>(original);
            result.addAll(added);
            return result;
        }
    }
}
