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
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.bleudev.nine_lifes.NineLifesClientData.lifes;
import static com.bleudev.nine_lifes.NineLifesClientData.should_death_screen_be_white;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {
    @Mutable
    @Shadow
    @Final
    private boolean hardcore;

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;<init>(Lnet/minecraft/network/chat/Component;)V"))
    private static Component changeTitle(Component original) {
        if (lifes <= 1 && !should_death_screen_be_white) {
            return Component.translatable("deathScreen.title.hardcore");
        } else return original;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void changeHardcore(Component component, boolean bl, CallbackInfo ci) {
        if (lifes <= 1 && !should_death_screen_be_white) this.hardcore = true;
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void renderWhitenessEffect(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if (should_death_screen_be_white)
            ClientInjectsKt.overlayWithColor(guiGraphics, 0xffffffff);
    }
}
