package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.client.util.ClientInjectsKt;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nine_lifes.NineLifesClientData.lifes;
import static com.bleudev.nine_lifes.NineLifesClientData.should_death_screen_be_white;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {
    @Mutable
    @Shadow
    @Final
    private boolean hardcore;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(Component component, boolean bl, CallbackInfo ci) {
        this.hardcore = lifes <= 1;
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void renderWhitenessEffect(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
//        ClientInjectsKt.overlayWithColor(guiGraphics, ClientInjectsKt.asColorWithAlpha(0xffffff, whiteness));
        if (should_death_screen_be_white)
            ClientInjectsKt.overlayWithColor(guiGraphics, 0xffffffff);
    }
}
