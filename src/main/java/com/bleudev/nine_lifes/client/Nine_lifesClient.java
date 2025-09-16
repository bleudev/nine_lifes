package com.bleudev.nine_lifes.client;

import com.bleudev.nine_lifes.ModDataStorage;
import com.bleudev.nine_lifes.client.compat.modmenu.ClothAutoConfig;
import com.bleudev.nine_lifes.client.custom.CustomEntityRenderers;
import com.bleudev.nine_lifes.networking.payloads.*;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL46;

import static com.bleudev.nine_lifes.Nine_lifes.MOD_ID;
import static net.minecraft.SharedConstants.TICKS_PER_SECOND;
import static net.minecraft.util.Formatting.DARK_AQUA;
import static net.minecraft.util.Formatting.RED;

public class Nine_lifesClient implements ClientModInitializer {
    public static Identifier OVERLAY_COLOR = Identifier.of(MOD_ID, "overlay_color");

    private boolean has_effect = false;

    private int armor_stand_hit_event_ticks = 0;
    private boolean armor_stand_hit_event_running = false;

    private float max_whiteness_screen = 0f;
    private int max_whiteness_screen_ticks = 0;
    private int whiteness_screen_ticks = 0;
    private boolean whiteness_screen_running = false;

    private float whiteness = 0f;
    private float redness = 0f;

    private float armor_stand_hit_redness = 0f;

    public static ClothAutoConfig getConfig() {
        return AutoConfig.getConfigHolder(ClothAutoConfig.class).getConfig();
    }

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ClothAutoConfig.class, JanksonConfigSerializer::new);

        CustomEntityRenderers.initialize();

        HudLayerRegistrationCallback.EVENT.register(layeredDrawerWrapper -> layeredDrawerWrapper
            .attachLayerAfter(IdentifiedLayer.OVERLAY_MESSAGE, OVERLAY_COLOR, this::renderOverlayColor));

        ClientTickEvents.END_CLIENT_TICK.register(this::tick);

        ClientPlayNetworking.registerGlobalReceiver(JoinMessagePayload.ID, ((payload, context) -> {
            if (getConfig().join_message_enabled) {
                boolean careful = payload.lives() <= 5;
                context.player().sendMessage(
                    (careful ? Text.translatable("chat.message.join.lives.careful", payload.lives()) :
                            Text.translatable("chat.message.join.lives", payload.lives())).formatted(careful ? RED : DARK_AQUA),
                    false);
            }
        }));
        ClientPlayNetworking.registerGlobalReceiver(UpdateCenterHeartPayload.ID, (payload, context) -> {
            ModDataStorage.lives = payload.lives();
        });
        ClientPlayNetworking.registerGlobalReceiver(AmethysmEffectUpdatePayload.ID, (payload, context) -> {
            has_effect = payload.has_effect();
        });
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
        WorldRenderEvents.AFTER_SETUP.register(context -> {
            if (has_effect)
                GL46.glColorMask(false, true, false, true);
            else
                GL46.glColorMask(true, true, true, true);
        });
    }

    private void renderOverlayColor(DrawContext context, RenderTickCounter tickCounter) {
        fillOverlayColor(context, ColorHelper.fromFloats(redness, 1f, 0f, 0f));
        fillOverlayColor(context, ColorHelper.fromFloats(whiteness, 1f, 1f, 1f));
    }

    private void fillOverlayColor(DrawContext context, int color) {
        context.fill(0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), color);
    }

    private void tick(MinecraftClient client) {
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
