package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.util.LivesUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {
    @Mutable
    @Shadow
    @Final
    private boolean isHardcore;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(Text message, boolean isHardcore, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if ((client.player != null) && (client.getServer() != null))
            this.isHardcore = LivesUtils.getLives(client.getServer().getPlayerManager().getPlayer(client.player.getUuid())) <= 1;
        else this.isHardcore = false;
    }
}
