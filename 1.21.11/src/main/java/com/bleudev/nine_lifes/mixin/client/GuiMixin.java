package com.bleudev.nine_lifes.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.level.storage.LevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.bleudev.nine_lifes.client.NineLifesClientStorageKt.getForceHardcore;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public class GuiMixin {
    @Redirect(method = "renderHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/LevelData;isHardcore()Z"))
    private boolean modifyIsHardcore(LevelData instance) {
        return getForceHardcore() || instance.isHardcore();
    }
}
