package com.bleudev.nine_lifes.client

import com.bleudev.nine_lifes.ISSUES_LINK
import com.bleudev.nine_lifes.NineLifesClientData
import com.bleudev.nine_lifes.NineLifesClientData.amethysm_effect_info
import com.bleudev.nine_lifes.NineLifesClientData.amethysm_purpleness
import com.bleudev.nine_lifes.NineLifesClientData.amethysm_whiteness
import com.bleudev.nine_lifes.NineLifesClientData.armor_stand_hit_event_running
import com.bleudev.nine_lifes.NineLifesClientData.armor_stand_hit_event_ticks
import com.bleudev.nine_lifes.NineLifesClientData.armor_stand_hit_redness
import com.bleudev.nine_lifes.NineLifesClientData.center_heart_info
import com.bleudev.nine_lifes.NineLifesClientData.charge_effect_info
import com.bleudev.nine_lifes.NineLifesClientData.getNextHeartbeatTime
import com.bleudev.nine_lifes.NineLifesClientData.heartbeat_ticks
import com.bleudev.nine_lifes.NineLifesClientData.lifes
import com.bleudev.nine_lifes.NineLifesClientData.max_heartbeat_ticks
import com.bleudev.nine_lifes.NineLifesClientData.max_whiteness_screen
import com.bleudev.nine_lifes.NineLifesClientData.max_whiteness_screen_ticks
import com.bleudev.nine_lifes.NineLifesClientData.redness
import com.bleudev.nine_lifes.NineLifesClientData.should_death_screen_be_white
import com.bleudev.nine_lifes.NineLifesClientData.whiteness
import com.bleudev.nine_lifes.NineLifesClientData.whiteness_screen_running
import com.bleudev.nine_lifes.NineLifesClientData.whiteness_screen_ticks
import com.bleudev.nine_lifes.api.event.client.ClientRespawnEvents
import com.bleudev.nine_lifes.client.config.HeartPosition
import com.bleudev.nine_lifes.client.config.configInit
import com.bleudev.nine_lifes.client.config.heartPosition
import com.bleudev.nine_lifes.client.config.joinMessageEnabled
import com.bleudev.nine_lifes.client.custom.NineLifesEntityRenderers
import com.bleudev.nine_lifes.client.util.asColorWithAlpha
import com.bleudev.nine_lifes.client.util.overlayWithColor
import com.bleudev.nine_lifes.custom.packet.payload.*
import com.bleudev.nine_lifes.util.createIdentifier
import com.bleudev.nine_lifes.util.lerp
import com.bleudev.nine_lifes.util.link
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
import net.minecraft.util.ARGB
import net.minecraft.world.level.GameType

class NineLifesClient : ClientModInitializer {
    object Layers {
        val OVERLAY_BEFORE_HOTBAR = createIdentifier("overlay_before_hotbar")
        val OVERLAY = createIdentifier("overlay")
        val LIFES_COUNT = createIdentifier("lifes_count")
    }

    object Sprites {
        val HARDCORE = createIdentifier("textures/hud/sprites/hardcore.png")
    }

    override fun onInitializeClient() {
        configInit()

        NineLifesEntityRenderers.initialize()

        HudElementRegistry.attachElementBefore(VanillaHudElements.HOTBAR, Layers.OVERLAY_BEFORE_HOTBAR) { g, _ -> renderOverlayBeforeHotBar(g) }
        HudElementRegistry.attachElementAfter(VanillaHudElements.HOTBAR, Layers.LIFES_COUNT) { g, _ -> renderLifesCount(g) }
        HudElementRegistry.addLast(Layers.OVERLAY) { g, _ -> renderOverlay(g) }

        ClientPlayNetworking.registerGlobalReceiver(AfterPlayerRespawn.id) { _, ctx ->
            ClientRespawnEvents.RESPAWN.invoker()(ctx.client())
        }
        ClientPlayNetworking.registerGlobalReceiver(JoinMessage.id) { payload, ctx ->
            if (joinMessageEnabled && (ctx.player().gameMode() ?: GameType.SURVIVAL).isSurvival) {
                val careful = payload.lifes <= 5
                ctx.player().sendSystemMessage(Component.translatable(
                    if (careful) "chat.message.join.lives.careful" else "chat.message.join.lives",
                    payload.lifes
                ).withStyle(if (careful) ChatFormatting.RED else ChatFormatting.DARK_AQUA))
            }
        }
        ClientPlayNetworking.registerGlobalReceiver(BetaModeMessage.id) { _, ctx -> ctx.player().sendSystemMessage(
            Component.translatable("chat.message.join.beta").append("\n").append(link(ISSUES_LINK))
            .withStyle(ChatFormatting.GOLD)) }
        ClientPlayNetworking.registerGlobalReceiver(UpdateLifesCount.id) { payload, _ -> lifes = payload.lifes }
        ClientPlayNetworking.registerGlobalReceiver(ArmorStandHitEvent.id) { _, _ ->
            if (!armor_stand_hit_event_running) {
                armor_stand_hit_event_running = true
                armor_stand_hit_event_ticks = 40
            }
        }
        ClientPlayNetworking.registerGlobalReceiver(StartWhitenessScreen.id) { payload, _ ->
            max_whiteness_screen_ticks = payload.duration
            max_whiteness_screen = payload.strength
            whiteness_screen_ticks = 0
            whiteness_screen_running = true
            should_death_screen_be_white = true
        }
        ClientPlayNetworking.registerGlobalReceiver(StartAmethysmScreen.id) { payload, _ ->
            amethysm_effect_info.start(payload.duration)
        }
        ClientPlayNetworking.registerGlobalReceiver(StartChargeScreen.id) { payload, _ ->
            charge_effect_info.start(payload.duration, payload.strength)
        }

        ClientTickEvents.END_CLIENT_TICK.register(::tick)
        ClientRespawnEvents.RESPAWN.register { _ ->
            should_death_screen_be_white = false
        }
    }

    private fun renderLifesCount(graphics: GuiGraphics) {
        val client = Minecraft.getInstance()
        if (client.player?.gameMode()?.isSurvival?.let { (!it) } == true) return

        val text = Component.literal(lifes.toString())

        val w: Int = graphics.guiWidth()
        val h: Int = graphics.guiHeight()
        val th = 18

        val delta = lifes.toFloat() / 9
        val color: Int = ARGB.colorFromFloat(0.75f, delta, delta, delta)

        graphics.pose().pushMatrix()

        val translatePosition = { dx: Int, dy: Int ->
            graphics.pose().translate(-th * center_heart_info.scale / 2 + dx, (h - 45 - (h - dy)).toFloat())
        }
        when (heartPosition) {
            HeartPosition.BOTTOM_LEFT -> translatePosition(25, h + 20)
            HeartPosition.BOTTOM_CENTER -> translatePosition(w / 2, h)
            HeartPosition.BOTTOM_RIGHT -> translatePosition(w - 25, h + 20)
            HeartPosition.TOP_LEFT -> translatePosition(25, 60)
            HeartPosition.TOP_CENTER -> translatePosition(w / 2, 60)
            HeartPosition.TOP_RIGHT -> translatePosition(w - 25, 60)
        }
        graphics.pose().scale(center_heart_info.scale)

        graphics.blit(RenderPipelines.GUI_TEXTURED, Sprites.HARDCORE,
            0, -5, 0f, 0f, th, th, th, th, color)
        graphics.drawString(client.font, text, client.font.width(text), 0, -0x1)

        graphics.pose().popMatrix()
    }

    private fun renderOverlayBeforeHotBar(graphics: GuiGraphics) {
        graphics.overlayWithColor(ARGB.colorFromFloat(0.5f * amethysm_purpleness, 0.5f, 0f, 0.5f))
        graphics.overlayWithColor(0xffffff.asColorWithAlpha(amethysm_whiteness))
        graphics.overlayWithColor(0xffffff.asColorWithAlpha(charge_effect_info.getWhiteness()))
    }

    private var lastMillis = 0L

    private fun renderOverlay(graphics: GuiGraphics) {
        graphics.overlayWithColor(0xff0000.asColorWithAlpha(redness))
        graphics.overlayWithColor(0xffffff.asColorWithAlpha(whiteness))

        val newTime = System.currentTimeMillis()
        if (lastMillis == 0L) lastMillis = newTime
        val deltaTime = (newTime - lastMillis).toFloat()
        lastMillis = newTime
        center_heart_info.tick(deltaTime / 50)
    }

    private fun tick(client: Minecraft) {
        NineLifesClientData.tick()
        if (max_heartbeat_ticks == 0) max_heartbeat_ticks = getNextHeartbeatTime(client.player)

        if (heartbeat_ticks == 0 && client.player != null && client.player!!.isAlive) center_heart_info.doHeartbeat(2f)
        heartbeat_ticks++
        if (heartbeat_ticks == max_heartbeat_ticks) {
            heartbeat_ticks = 0
            max_heartbeat_ticks = getNextHeartbeatTime(client.player)
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

        redness = armor_stand_hit_redness.lerp(end = .2f)

        if (whiteness_screen_running) {
            if (whiteness_screen_ticks == max_whiteness_screen_ticks) whiteness_screen_running = false
            else {
                whiteness_screen_ticks++
                whiteness = (whiteness_screen_ticks.toFloat() / max_whiteness_screen_ticks).lerp(end = max_whiteness_screen)
            }
        } else whiteness = 0f
    }
}