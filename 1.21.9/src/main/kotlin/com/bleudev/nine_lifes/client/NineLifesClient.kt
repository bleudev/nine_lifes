package com.bleudev.nine_lifes.client

import com.bleudev.nine_lifes.ISSUES_LINK
import com.bleudev.nine_lifes.MOD_ID
import com.bleudev.nine_lifes.NineLifesClientData
import com.bleudev.nine_lifes.NineLifesClientData.amethysm_effect_info
import com.bleudev.nine_lifes.NineLifesClientData.amethysm_purpleness
import com.bleudev.nine_lifes.NineLifesClientData.armor_stand_hit_event_running
import com.bleudev.nine_lifes.NineLifesClientData.armor_stand_hit_event_ticks
import com.bleudev.nine_lifes.NineLifesClientData.armor_stand_hit_redness
import com.bleudev.nine_lifes.NineLifesClientData.center_heart_info
import com.bleudev.nine_lifes.NineLifesClientData.charge_effect_info
import com.bleudev.nine_lifes.NineLifesClientData.get_next_heartbeat_time
import com.bleudev.nine_lifes.NineLifesClientData.heartbeat_ticks
import com.bleudev.nine_lifes.NineLifesClientData.lifes
import com.bleudev.nine_lifes.NineLifesClientData.max_heartbeat_ticks
import com.bleudev.nine_lifes.NineLifesClientData.max_whiteness_screen
import com.bleudev.nine_lifes.NineLifesClientData.max_whiteness_screen_ticks
import com.bleudev.nine_lifes.NineLifesClientData.question_marks
import com.bleudev.nine_lifes.NineLifesClientData.redness
import com.bleudev.nine_lifes.NineLifesClientData.whiteness
import com.bleudev.nine_lifes.NineLifesClientData.whiteness_screen_running
import com.bleudev.nine_lifes.NineLifesClientData.whiteness_screen_ticks
import com.bleudev.nine_lifes.client.compat.modmenu.NineLifesConfig
import com.bleudev.nine_lifes.client.compat.modmenu.NineLifesConfig.HeartPosition
import com.bleudev.nine_lifes.client.util.anaglyph
import com.bleudev.nine_lifes.client.util.overlayWithColor
import com.bleudev.nine_lifes.custom.packet.payload.*
import com.bleudev.nine_lifes.util.createResourceLocation
import com.bleudev.nine_lifes.util.lerp
import com.bleudev.nine_lifes.util.link
import eu.midnightdust.lib.config.MidnightConfig
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.ChatFormatting
import net.minecraft.SharedConstants
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.ARGB
import net.minecraft.world.level.GameType

class NineLifesClient : ClientModInitializer {
    object Layers {
        val OVERLAY_BEFORE_HOTBAR = createResourceLocation("overlay_before_hotbar")
        val OVERLAY = createResourceLocation("overlay")
        val LIFES_COUNT = createResourceLocation("lifes_count")
    }

    object Sprites {
        val QUESTION_MARK = createResourceLocation("textures/hud/sprites/question_mark.png")
    }

    override fun onInitializeClient() {
        MidnightConfig.init(MOD_ID, NineLifesConfig::class.java)

        CustomEntityRenderers.initialize()

        HudElementRegistry.attachElementBefore(VanillaHudElements.HOTBAR, Layers.OVERLAY_BEFORE_HOTBAR) { g, _ -> renderOverlayBeforeHotBar(g) }
        HudElementRegistry.attachElementAfter(VanillaHudElements.HOTBAR, Layers.LIFES_COUNT) { g, _ -> renderLifesCount(g) }
        HudElementRegistry.addLast(Layers.OVERLAY) { g, _ -> renderOverlay(g) }

        ClientTickEvents.END_CLIENT_TICK.register(::tick)

        ClientPlayNetworking.registerGlobalReceiver(JoinMessage.id) { payload, ctx ->
            if (NineLifesConfig.join_message_enabled && (ctx.player().gameMode() ?: GameType.SURVIVAL).isSurvival) {
                val careful = payload.lifes <= 5
                ctx.player().displayClientMessage(Component.translatable(
                    if (careful) "chat.message.join.lives.careful" else "chat.message.join.lives",
                    payload.lifes
                ).withStyle(if (careful) ChatFormatting.RED else ChatFormatting.DARK_AQUA), false)
            }
        }
        ClientPlayNetworking.registerGlobalReceiver(BetaModeMessage.id) { _, ctx ->
            ctx.player().displayClientMessage(Component.translatable("chat.message.join.beta")
                .append("\n").append(link(ISSUES_LINK)).withStyle(ChatFormatting.GOLD), false)
        }
        ClientPlayNetworking.registerGlobalReceiver(UpdateLifesCount.id) { payload, _ -> lifes = payload.lifes }
        ClientPlayNetworking.registerGlobalReceiver(ArmorStandHitEvent.id) { _, _ ->
            if (!armor_stand_hit_event_running) runArmorStandHitEvent()
        }
        ClientPlayNetworking.registerGlobalReceiver(StartWhitenessScreen.id) { payload, _ ->
            max_whiteness_screen_ticks = payload.duration
            max_whiteness_screen = payload.strength
            whiteness_screen_ticks = 0
            whiteness_screen_running = true
        }
        ClientPlayNetworking.registerGlobalReceiver(StartAmethysmScreen.id) { payload, _ ->
            amethysm_effect_info.start(payload.duration)
        }
        ClientPlayNetworking.registerGlobalReceiver(StartChargeScreen.id) { payload, _ ->
            charge_effect_info.start(payload.duration, payload.strength)
        }
    }

    private fun renderLifesCount(context: GuiGraphics) {
        val client = Minecraft.getInstance()
        if (client.player?.gameMode()?.isSurvival?.let { (!it) } == true) return

        val text = Component.literal(lifes.toString())

        val w: Int = context.guiWidth()
        val h: Int = context.guiHeight()
        val th = 18

        val delta = lifes.toFloat() / 9
        val color: Int = ARGB.colorFromFloat(0.75f, delta, delta, delta)

        val drawCenterHeart = { texture: ResourceLocation ->
            context.blitSprite(RenderPipelines.GUI_TEXTURED, texture,
                0, -5, 0, 0, th, th, th, th, color) }

        context.pose().pushMatrix()
        val v = center_heart_info.offsetAndScale
        val translatePosition = { dx: Int, dy: Int ->
            context.pose().translate((-th * v.z()).toFloat() / 2 + dx, (h - 45 - (h - dy)).toFloat())
        }

        when (NineLifesConfig.heart_position) {
            HeartPosition.BOTTOM_LEFT -> translatePosition(25, h + 20)
            HeartPosition.BOTTOM_CENTER -> translatePosition(w / 2, h)
            HeartPosition.BOTTOM_RIGHT -> translatePosition(w - 25, h + 20)
            HeartPosition.TOP_LEFT -> translatePosition(25, 60)
            HeartPosition.TOP_CENTER -> translatePosition(w / 2, 60)
            HeartPosition.TOP_RIGHT -> translatePosition(w - 25, 60)
        }

        context.pose().translate(v.x().toFloat(), v.y().toFloat())
        context.pose().scale(v.z().toFloat())
        drawCenterHeart(ResourceLocation.withDefaultNamespace("textures/gui/sprites/hud/heart/container_hardcore.png"))
        drawCenterHeart(ResourceLocation.withDefaultNamespace("textures/gui/sprites/hud/heart/hardcore_full.png"))
        context.drawString(client.font, text, client.font.width(text), 0, -0x1)
        context.pose().popMatrix()
    }

    private fun renderOverlayBeforeHotBar(guiGraphics: GuiGraphics) {
        guiGraphics.overlayWithColor(ARGB.colorFromFloat(0.5f * amethysm_purpleness, 0.5f, 0f, 0.5f))
        guiGraphics.overlayWithColor(ARGB.colorFromFloat(charge_effect_info.getWhiteness(), 1f, 1f, 1f))
    }

    private var lastMillis = 0L

    private fun renderOverlay(guiGraphics: GuiGraphics) {
        guiGraphics.overlayWithColor(ARGB.setBrightness(0xffffff, amethysm_purpleness))
        guiGraphics.overlayWithColor(ARGB.setBrightness(0xff0000, redness))
        guiGraphics.overlayWithColor(ARGB.setBrightness(0xffffff, whiteness))

        // Question marks
        val newTime = System.currentTimeMillis()
        if (lastMillis == 0L) lastMillis = newTime
        val deltaTime = (newTime - lastMillis).toFloat()
        lastMillis = newTime

        center_heart_info.tick(deltaTime / 50)

        val w: Int = guiGraphics.guiWidth()
        val h: Int = guiGraphics.guiHeight()
        val qmh = h / 5

        question_marks.forEach { i ->
            val v = i.tick(deltaTime / 50)
            guiGraphics.pose().pushMatrix()
            guiGraphics.pose().translate((w * v.x()).toFloat() - qmh.toFloat() / 2, (h * v.y()).toFloat() - qmh.toFloat() / 2)
            guiGraphics.anaglyph({ c: Int ->
                    guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Sprites.QUESTION_MARK,
                        0, 0, 0, 0, qmh, qmh, qmh, qmh, c) },
                i.offset, baseColor = ARGB.setBrightness(0xffffff, v.z().toFloat()))
            guiGraphics.pose().popMatrix()
        }
        question_marks.removeIf { i -> i.time >= i.duration }
    }

    private fun tick(client: Minecraft) {
        NineLifesClientData.tick()
        if (max_heartbeat_ticks == 0) max_heartbeat_ticks = get_next_heartbeat_time(client.player)

        if (heartbeat_ticks == 0 && client.player != null && client.player!!.isAlive) center_heart_info.do_heartbeat(2f)
        heartbeat_ticks++
        if (heartbeat_ticks == max_heartbeat_ticks) {
            heartbeat_ticks = 0
            max_heartbeat_ticks = get_next_heartbeat_time(client.player)
        }

        if (armor_stand_hit_event_running) {
            if (armor_stand_hit_event_ticks == 0) armor_stand_hit_event_running = false
            else {
                armor_stand_hit_event_ticks--

                val fromTicks = 2 * SharedConstants.TICKS_PER_SECOND - armor_stand_hit_event_ticks

                armor_stand_hit_redness = if (fromTicks <= .5 * SharedConstants.TICKS_PER_SECOND) (fromTicks / (.5f * SharedConstants.TICKS_PER_SECOND)).lerp()
                else ((fromTicks - .5f * SharedConstants.TICKS_PER_SECOND) / (1.5f * SharedConstants.TICKS_PER_SECOND)).lerp(1f, 0f)
            }
        }

        redness = armor_stand_hit_redness.lerp(0f, .2f)

        if (whiteness_screen_running) {
            if (whiteness_screen_ticks == max_whiteness_screen_ticks) whiteness_screen_running = false
            else {
                whiteness_screen_ticks++
                whiteness = (whiteness_screen_ticks.toFloat() / max_whiteness_screen_ticks).lerp(end = max_whiteness_screen)
            }
        } else whiteness = 0f
    }

    private fun runArmorStandHitEvent() {
        armor_stand_hit_event_running = true
        armor_stand_hit_event_ticks = 2 * SharedConstants.TICKS_PER_SECOND
    }
}