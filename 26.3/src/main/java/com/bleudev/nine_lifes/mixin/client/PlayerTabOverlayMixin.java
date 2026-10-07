package com.bleudev.nine_lifes.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;

import java.util.UUID;

import static com.bleudev.nine_lifes.client.NineLifesClientStorageKt.getPlayersLifesCount;
import static com.bleudev.nine_lifes.client.util.LifesCountUtilKt.getLifesCountTabStyle;

@Environment(EnvType.CLIENT)
@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {
    @WrapMethod(method = "getNameForDisplay")
    private Component addLifesCount(PlayerInfo info, Operation<Component> original) {
        UUID id = info.getProfile().id();
        Component name = original.call(info);

        Integer lifesCount;
        ChatFormatting formatting;
        if ((lifesCount = getPlayersLifesCount().get(id)) != null &&
            (formatting = getLifesCountTabStyle(lifesCount)) != null &&
            info.getGameMode().isSurvival()
        ) {
            return name.copy().append(Component
                .literal(" (" + lifesCount + ")")
                .withStyle(formatting)
            );
        }
        return name;
    }
}
