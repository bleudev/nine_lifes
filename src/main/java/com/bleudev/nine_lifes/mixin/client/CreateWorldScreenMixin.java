package com.bleudev.nine_lifes.mixin.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin extends Screen {
    @Shadow
    @Nullable
    private TabNavigationWidget tabNavigation;
    @Shadow
    @Final
    private TabManager tabManager;
    @Unique
    private boolean nineLives = false;
    @Unique
    private ButtonWidget nineLivesButton;

    protected CreateWorldScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addNineLivesButton(CallbackInfo ci) {
        int x = this.width / 2 + 110;
        int y = this.height / 4 - 15;

        nineLivesButton = ButtonWidget.builder(getNineLivesText(), b -> {
            nineLives = !nineLives;
            b.setMessage(getNineLivesText());
        }).dimensions(x, y, 20, 20).build();

        this.addDrawableChild(nineLivesButton);
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void render(DrawContext context, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
        if (this.tabManager.getCurrentTab() != null) {
            System.out.println(this.tabManager.getCurrentTab().getTitle().toString());
            nineLivesButton.visible = this.tabManager.getCurrentTab().getTitle().equals(Text.translatable("createWorld.tab.game.title"));
        } else {
            System.out.println("why!");
        }
    }

    @Unique
    private Text getNineLivesText() {
        return nineLives ? Text.literal("☑") : Text.literal("☐");
    }
}
