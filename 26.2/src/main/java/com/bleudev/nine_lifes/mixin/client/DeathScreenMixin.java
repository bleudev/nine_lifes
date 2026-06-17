package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.client.util.ClientInjectsKt;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static com.bleudev.nine_lifes.client.NineLifesClientStorageKt.*;
import static com.bleudev.nine_lifes.util.TranslationUtilsKt.deathScreenRemaining;

@Mixin(DeathScreen.class)
public class DeathScreenMixin extends Screen {
    @Unique
    private static final Component HARDCORE_TITLE = Component.translatable("deathScreen.title.hardcore");
    @Unique
    private static final Component VANILLA_TITLE = Component.translatable("deathScreen.title");

    @Unique
    private Component getNewTitle(int lifesCount) {
        if (getForceHardcoreDeathScreen()) {
            return HARDCORE_TITLE;
        } else {
            if (lifesCount >= 9) return VANILLA_TITLE;
            MutableComponent result = Component.translatable(deathScreenRemaining(lifesCount));
            if (lifesCount == 1) return result.withStyle(ChatFormatting.RED);
            return result;
        }
    }

    @Mutable
    @Shadow
    @Final
    private boolean hardcore;

    protected DeathScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void changeHardcore(Component causeOfDeath, boolean hardcore, LocalPlayer player, CallbackInfo ci) {
        if (getForceHardcoreDeathScreen() || getLifes() == 1) this.hardcore = true;
    }

    @Inject(method = "extractRenderState", at = @At("HEAD"))
    private void renderWhitenessEffect(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        if (should_death_screen_be_white) {
            ClientInjectsKt.white(graphics);
        }
    }

    @ModifyArgs(method = "visitText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/ActiveTextCollector;accept(Lnet/minecraft/client/gui/TextAlignment;IILnet/minecraft/network/chat/Component;)V"))
    private void modifyTitle(Args args) {
        if (((int)args.get(2)) == 30) args.set(3, getNewTitle(getLifes()));
    }
}
