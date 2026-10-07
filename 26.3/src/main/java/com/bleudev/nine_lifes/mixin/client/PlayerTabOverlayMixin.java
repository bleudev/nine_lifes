package com.bleudev.nine_lifes.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;

import static com.bleudev.nine_lifes.client.util.LifesCountUtilKt.addLifesCountToName;

@Environment(EnvType.CLIENT)
@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
    @WrapMethod(method = "getNameForDisplay")
    private Component addLifesCount(PlayerInfo info, Operation<Component> original) {
        Component name = original.call(info);
        return info.getGameMode().isSurvival() ? addLifesCountToName(name, info.getProfile().id()) : name;
    }
}
