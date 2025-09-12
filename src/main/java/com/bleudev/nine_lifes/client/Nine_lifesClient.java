package com.bleudev.nine_lifes.client;

import com.bleudev.nine_lifes.ModDataStorage;
import com.bleudev.nine_lifes.client.compat.modmenu.ClothAutoConfig;
import com.bleudev.nine_lifes.client.custom.CustomEntityRenderers;
import com.bleudev.nine_lifes.networking.payloads.AmethysmEffectUpdatePayload;
import com.bleudev.nine_lifes.networking.payloads.ArmorStandHitEventPayload;
import com.bleudev.nine_lifes.networking.payloads.JoinMessagePayload;
import com.bleudev.nine_lifes.networking.payloads.UpdateCenterHeartPayload;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
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

        HudElementRegistry.addLast(OVERLAY_COLOR, this::renderOverlayColor);

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
    }

    private void runArmorStandHitEvent() {
        armor_stand_hit_event_running = true;
        armor_stand_hit_event_ticks = 2 * TICKS_PER_SECOND;
    }
}
