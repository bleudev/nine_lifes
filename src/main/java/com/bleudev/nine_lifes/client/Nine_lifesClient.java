package com.bleudev.nine_lifes.client;

import com.bleudev.nine_lifes.client.compat.modmenu.ModMenuConfig;
import com.bleudev.nine_lifes.client.custom.CustomEntityRenderers;
import com.bleudev.nine_lifes.networking.payloads.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;

import static com.bleudev.nine_lifes.ClientModStorage.*;
import static com.bleudev.nine_lifes.Nine_lifes.MOD_ID;
import static com.bleudev.nine_lifes.client.util.RenderingUtils.renderAnaglyph;
import static net.minecraft.SharedConstants.TICKS_PER_SECOND;
import static net.minecraft.util.Formatting.DARK_AQUA;
import static net.minecraft.util.Formatting.RED;

public class Nine_lifesClient implements ClientModInitializer {
    public static final Identifier OVERLAY_COLOR_BEFORE_HOTBAR = Identifier.of(MOD_ID, "overlay_color_before_hotbar");
    public static final Identifier OVERLAY_COLOR = Identifier.of(MOD_ID, "overlay_color");
    public static final Identifier CENTER_HEART = Identifier.of(MOD_ID, "center_heart");

    private static final Identifier QUESTION_MARK = Identifier.of(MOD_ID, "textures/hud/sprites/question_mark.png");

    public static ModMenuConfig.ClothConfig getConfig() {
        return AutoConfig.getConfigHolder(ModMenuConfig.ClothConfig.class).getConfig();
    }

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModMenuConfig.ClothConfig.class, JanksonConfigSerializer::new);

        CustomEntityRenderers.initialize();

        HudElementRegistry.attachElementBefore(VanillaHudElements.HOTBAR, OVERLAY_COLOR_BEFORE_HOTBAR, this::renderOverlayColorBeforeHotBar);
        HudElementRegistry.attachElementAfter(VanillaHudElements.HOTBAR, CENTER_HEART, this::renderCenterHeart);
        HudElementRegistry.addLast(OVERLAY_COLOR, this::renderOverlayColor);

        ClientTickEvents.END_CLIENT_TICK.register(this::tick);

        ClientPlayNetworking.registerGlobalReceiver(JoinMessagePayload.ID, ((payload, context) -> {
            if (getConfig().join_message_enabled) {
                boolean careful = payload.lives() <= 5;
                context.player().sendMessage(
                    Text.translatable(careful ? "chat.message.join.lives.careful" : "chat.message.join.lives", payload.lives())
                    .formatted(careful ? RED : DARK_AQUA), false);
            }
        }));
        ClientPlayNetworking.registerGlobalReceiver(UpdateCenterHeartPayload.ID, (payload, context) -> lives = payload.lives());
        ClientPlayNetworking.registerGlobalReceiver(ArmorStandHitEventPayload.ID, (payload, context) -> {
            if (!armor_stand_hit_event_running)
                runArmorStandHitEvent();
        });
        ClientPlayNetworking.registerGlobalReceiver(StartWhitenessScreenEvent.ID, (payload, context) -> {
            max_whiteness_screen_ticks = payload.duration();
            max_whiteness_screen = payload.max_whiteness();
            whiteness_screen_ticks = 0;
            whiteness_screen_running = true;
        });
        ClientPlayNetworking.registerGlobalReceiver(StartAmethysmScreenEffectPayload.ID, (payload, context) -> {
            amethysm_effect_info.start(payload.effect_ticks());
        });
    }

    private void renderCenterHeart(DrawContext context, RenderTickCounter tickCounter) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        Text text = Text.literal("" + lives);

        int w = context.getScaledWindowWidth();
        int h = context.getScaledWindowHeight();
        int th = 18;

        float delta = (float) lives / 9;
        int color = ColorHelper.fromFloats(0.75f, delta, delta, delta);

        Consumer<Identifier> drawCenterHeart = texture ->
            context.drawTexture(RenderPipelines.GUI_TEXTURED, texture, 0, -5, 0, 0, th, th, th, th, color);

        context.getMatrices().pushMatrix();
        context.getMatrices().translate((float) (w - th) / 2, h - 45);
        drawCenterHeart.accept(Identifier.ofVanilla("textures/gui/sprites/hud/heart/container_hardcore.png"));
        drawCenterHeart.accept(Identifier.ofVanilla("textures/gui/sprites/hud/heart/hardcore_full.png"));
        context.drawTextWithShadow(textRenderer, text, textRenderer.getWidth(text), 0, 0xffffffff);
        context.getMatrices().popMatrix();
    }

    private void renderOverlayColorBeforeHotBar(DrawContext context, RenderTickCounter tickCounter) {
        Consumer<Integer> fill_overlay_color = color -> renderOverlayColor(context, color);

        fill_overlay_color.accept(ColorHelper.fromFloats(0.5f * amethysm_purpleness, 0.5f, 0f, 0.5f));
    }

    private long lastMillis = 0;

    private void renderOverlayColor(DrawContext context, RenderTickCounter tickCounter) {
        Consumer<Integer> fill_overlay_color = color -> renderOverlayColor(context, color);

        fill_overlay_color.accept(ColorHelper.withAlpha(amethysm_whiteness, 0xffffff));

        fill_overlay_color.accept(ColorHelper.withAlpha(redness, 0xff0000));
        fill_overlay_color.accept(ColorHelper.withAlpha(whiteness, 0xffffff));

        // Question marks
        long new_time = System.currentTimeMillis();
        if (lastMillis == 0) lastMillis = new_time;
        float delta_time = new_time - lastMillis;
        lastMillis = new_time;

        int w = context.getScaledWindowWidth();
        int h = context.getScaledWindowHeight();
        int qmh = h / 5;

        question_marks.forEach(i -> {
            var v = i.tick(delta_time / 50);
            context.getMatrices().pushMatrix();
            context.getMatrices().translate((float) (w * v.getX()) - (float) qmh / 2, (float) (h * v.getY()) - (float) qmh / 2);
            renderAnaglyph(context.getMatrices(),
                c -> context.drawTexture(RenderPipelines.GUI_TEXTURED, QUESTION_MARK, 0, 0, 0, 0, qmh, qmh, qmh, qmh, c),
                i.getOffset(), ColorHelper.withAlpha((float) v.getZ(), 0xffffff));
            context.getMatrices().popMatrix();
        });
        question_marks.removeIf(i -> i.getTime() >= i.getDuration());
    }

    private void renderOverlayColor(DrawContext context, int color) {
        context.fill(0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), color);
    }

    private void tick(MinecraftClient client) {
        amethysm_effect_info.tick();

        if (armor_stand_hit_event_running) {
            if (armor_stand_hit_event_ticks == 0)
                armor_stand_hit_event_running = false;
            else {
                armor_stand_hit_event_ticks--;

                var from_ticks = 2 * TICKS_PER_SECOND - armor_stand_hit_event_ticks;

                if (from_ticks <= .5 * TICKS_PER_SECOND)
                    armor_stand_hit_redness = MathHelper.lerp(from_ticks / (.5f * TICKS_PER_SECOND), 0f, 1f);
                else
                    armor_stand_hit_redness = MathHelper.lerp((from_ticks - .5f * TICKS_PER_SECOND) / (1.5f * TICKS_PER_SECOND), 1f, 0f);
            }
        }

        redness = MathHelper.lerp(armor_stand_hit_redness, 0f, .2f);

        if (whiteness_screen_running) {
            if (whiteness_screen_ticks == max_whiteness_screen_ticks)
                whiteness_screen_running = false;
            else {
                whiteness_screen_ticks++;

                whiteness = MathHelper.lerp((float) whiteness_screen_ticks / max_whiteness_screen_ticks, 0f, max_whiteness_screen);
            }
        }
        else whiteness = 0f;
    }

    private void runArmorStandHitEvent() {
        armor_stand_hit_event_running = true;
        armor_stand_hit_event_ticks = 2 * TICKS_PER_SECOND;
    }
}
