package com.bleudev.nine_lifes.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.level.storage.LevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.bleudev.nine_lifes.client.NineLifesClientStorageKt.getForceHardcoreHearts;

@Environment(EnvType.CLIENT)
@Mixin(Hud.class)
public class HudMixin {
    @Redirect(method = "extractHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/LevelData;isHardcore()Z"))
    private boolean modifyIsHardcore(LevelData instance) {
        return getForceHardcoreHearts() || instance.isHardcore();
    }
}
