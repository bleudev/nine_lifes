package com.bleudev.nine_lifes.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.bleudev.nine_lifes.NineLifesClientData.lifes;

@Mixin(ClientLevel.ClientLevelData.class)
public class ClientLevelDataMixin {
    @Inject(method = "getDayTime", at = @At("HEAD"), cancellable = true)
    private void setAlwaysDay(CallbackInfoReturnable<Long> cir) {
        var pl = Minecraft.getInstance().player;
        if (lifes <= 3 && pl != null) {
            var gm = pl.gameMode();
            if (gm != null && gm.isSurvival())
                cir.setReturnValue(9000L);
        }
    }
}
