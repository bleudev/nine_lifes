package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.NineLifesClientData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectsInInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(EffectsInInventory.class)
public class EffectsInInventoryMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void stopRenderIfStickUsed(GuiGraphics graphics, int mouseX, int mouseY, CallbackInfo ci) {
        if (NineLifesClientData.stickUsedTicks > 0) ci.cancel();
    }
}
