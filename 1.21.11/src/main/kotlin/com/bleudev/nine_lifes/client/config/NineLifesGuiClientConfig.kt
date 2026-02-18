package com.bleudev.nine_lifes.client.config

import com.bleudev.nine_lifes.MOD_ID
import com.bleudev.nine_lifes.util.createIdentifier
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.OptionEventListener
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder
import dev.isxander.yacl3.api.controller.EnumControllerBuilder
import dev.isxander.yacl3.dsl.OptionDsl
import dev.isxander.yacl3.dsl.YetAnotherConfigLib
import dev.isxander.yacl3.dsl.descriptionBuilder
import dev.isxander.yacl3.gui.image.ImageRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import kotlin.reflect.KMutableProperty

private var cachedJoinMessage: Boolean? = null
private var cachedHeartbeat: Boolean? = null
private var cachedHeartPosition: HeartPosition? = null
private var cachedLowLifesRedSky: Boolean? = null

fun generateGuiConfigScreen(parent: Screen?): Screen = YetAnotherConfigLib(MOD_ID) {
    categories.register("general") {
        rootOptions.register("join_message") {
            binding(true, ::joinMessageEnabled)
            yesNoFormat()
            cachePending(::cachedJoinMessage::set)
            descriptionBuilder {
                addDefaultText(1)
                localisedConfigImage("join_message") {cachedJoinMessage ?: joinMessageEnabled}
            }
        }
        rootOptions.register("heartbeat") {
            binding(true, ::heartbeatEnabled)
            yesNoFormat()
            cachePending(::cachedHeartbeat::set)
            descriptionBuilder {
                addDefaultText(1)
            }
        }
        rootOptions.register("heart_position") {
            binding(HeartPosition.BOTTOM_CENTER, ::heartPosition)
            enumFormat()
            cachePending(::cachedHeartPosition::set)
            descriptionBuilder {
                addDefaultText(1)
                enumConfigImage("heart_position") {cachedHeartPosition ?: heartPosition}
            }
        }
        rootOptions.register("low_lifes_red_sky") {
            binding(true, ::lowLifesRedSkyEnabled)
            yesNoFormat()
            cachePending(::cachedLowLifesRedSky::set)
            descriptionBuilder {
                addDefaultText(1)
                localisedConfigImage("low_lifes_red_sky") {cachedLowLifesRedSky ?: lowLifesRedSkyEnabled}
            }
        }
    }
}.generateScreen(parent)

private fun OptionDsl<Boolean>.yesNoFormat() = controller { BooleanControllerBuilder.create(it).yesNoFormatter().coloured(true) }
private inline fun <reified T : Enum<T>> OptionDsl<T>.enumFormat() = controller { EnumControllerBuilder.create(it).enumClass(T::class.java) }
private fun <T : Any> OptionDsl<T>.binding(default: T, property: KMutableProperty<T>) = binding(default, {property.getter.call()}, {property.setter.call(it)})
private fun <T : Any> OptionDsl<T>.cachePending(cacher: (T) -> Unit) = addListener { option, event -> if (event == OptionEventListener.Event.STATE_CHANGE) cacher(option.pendingValue()) }
private fun OptionDescription.Builder.localisedConfigImage(name: String, condition: (() -> Boolean) = {true}) = customImage(LocalisedImageRenderer(name, condition))
private fun <T : Enum<T>> OptionDescription.Builder.enumConfigImage(name: String, enumGetter: () -> T) = customImage(EnumImageRenderer(name, enumGetter))

private abstract class MethodBasedImageRenderer : ImageRenderer {
    override fun render(
        graphics: GuiGraphics,
        x: Int,
        y: Int,
        renderWidth: Int,
        tickDelta: Float
    ): Int {
        val id = getImagePath()
        val t = Minecraft.getInstance().textureManager.getTexture(id)
        val h = renderWidth * t.texture.getHeight(0) / t.texture.getWidth(0)
        graphics.blit(RenderPipelines.GUI_TEXTURED, id, x, y, 0f, 0f, renderWidth, h, renderWidth, h)
        return h
    }

    override fun close() {
    }

    abstract fun getImagePath(): Identifier
}

private class LocalisedImageRenderer(val name: String, val condition: () -> Boolean) : MethodBasedImageRenderer() {
    override fun getImagePath(): Identifier {
        val mc = Minecraft.getInstance()
        val lang = mc.options.languageCode
        val langPath = "textures/config/description/$lang/$name.png"
        val langId = createIdentifier(langPath)
        val fallbackId = createIdentifier("textures/config/description/$name.png")
        val disabledId = createIdentifier("textures/config/description/${name}_disabled.png")

        if (!condition()) return disabledId
        return if (LocalisedImageRenderer::class.java.classLoader.getResource("assets/nine_lifes/$langPath") == null)
            fallbackId else langId
    }
}

private class EnumImageRenderer<T : Enum<T>>(val name: String, val enumGetter: () -> T) : MethodBasedImageRenderer() {
    override fun getImagePath(): Identifier = createIdentifier(
        "textures/config/description/$name/${enumGetter().ordinal}.png"
    )
}