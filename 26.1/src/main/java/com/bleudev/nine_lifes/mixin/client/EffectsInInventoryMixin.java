package com.bleudev.nine_lifes.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nine_lifes.client.NineLifesClientStorageKt.stickUsedTicks;

@Environment(EnvType.CLIENT)
@Mixin(EffectsInInventory.class)
public class EffectsInInventoryMixin {
    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void stopRenderIfStickUsed(GuiGraphicsExtractor graphics, int mouseX, int mouseY, CallbackInfo ci) {
        if (stickUsedTicks > 0) ci.cancel();
    }
}
