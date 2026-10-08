package com.bleudev.nine_lifes.client.config

import com.bleudev.nine_lifes.client.config.client.HealthRendering
import com.bleudev.nine_lifes.util.createIdentifier
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.OptionEventListener
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder
import dev.isxander.yacl3.api.controller.EnumControllerBuilder
import dev.isxander.yacl3.dsl.OptionDsl
import dev.isxander.yacl3.gui.image.ImageRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import kotlin.reflect.KMutableProperty

internal fun OptionDsl<Boolean>.yesNoFormat() = controller { BooleanControllerBuilder.create(it).yesNoFormatter().coloured(true) }
internal inline fun <reified T : Enum<T>> OptionDsl<T>.enumFormat() = controller { EnumControllerBuilder.create(it).enumClass(T::class.java) }
internal fun <T : Any> OptionDsl<T>.binding(default: T, property: KMutableProperty<T>) = binding(default, {property.getter.call()}, {property.setter.call(it)})
internal fun <T : Any> OptionDsl<T>.cachePending(cacher: (T) -> Unit) = addListener { option, event -> if (event == OptionEventListener.Event.STATE_CHANGE) cacher(option.pendingValue()) }
internal fun OptionDescription.Builder.conditionConfigImage(name: String, condition: () -> Boolean) = customImage(object : MethodBasedImageRenderer() {
    override fun getImagePath(): Identifier {
        var p = "textures/config/description/$name"
        if (!condition()) {
            p += "_disabled"
        }
        return createIdentifier("$p.png")
    }
})
internal fun OptionDescription.Builder.localisedConfigImage(name: String, condition: () -> Boolean = {true}) = customImage(LocalisedImageRenderer(name, false, condition))
internal fun OptionDescription.Builder.fullLocalisedConfigImage(name: String, condition: () -> Boolean = {true}) = customImage(LocalisedImageRenderer(name, true, condition))
internal fun <T : Enum<T>> OptionDescription.Builder.enumConfigImage(name: String, enumGetter: () -> T) = customImage(EnumImageRenderer(name, enumGetter))
internal fun OptionDescription.Builder.framedConfigImage(name: String, duration: Int, fps: Int, condition: () -> Boolean = {true}, useFirstFrameWhenFalse: Boolean = false) = customImage(
    AnimatedConditionImageRenderer(name, { ((it % (duration * 1000 / fps)) / (1000 / fps)).toInt() }, condition, useFirstFrameWhenFalse))

internal abstract class MethodBasedImageRenderer : ImageRenderer {
    override fun render(graphics: GuiGraphicsExtractor, x: Int, y: Int, renderWidth: Int, tickDelta: Float): Int {
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

internal class LocalisedImageRenderer(val name: String, val localiseDisabled: Boolean = false, val condition: () -> Boolean) : MethodBasedImageRenderer() {
    override fun getImagePath(): Identifier {
        val mc = Minecraft.getInstance()
        val lang = mc.options.languageCode

        var fallbackPath = "textures/config/description/$name"
        var langPath = "textures/config/description/$lang/$name"
        if (condition()) {
            fallbackPath += ".png"
            langPath += ".png"
        } else {
            fallbackPath += "_disabled.png"
            langPath += "_disabled.png"
            if (!localiseDisabled) return createIdentifier(fallbackPath)
        }
        return createIdentifier(if (LocalisedImageRenderer::class.java.classLoader.getResource("assets/nine_lifes/$langPath") == null)
            fallbackPath else langPath)
    }
}

internal class EnumImageRenderer<T : Enum<T>>(val name: String, val enumGetter: () -> T) : MethodBasedImageRenderer() {
    override fun getImagePath(): Identifier = createIdentifier(
        "textures/config/description/$name/${enumGetter().ordinal}.png"
    )
}

internal class AnimatedConditionImageRenderer(val name: String, val frameProvider: (Long) -> Int, val condition: () -> Boolean, val useFirstFrameWhenFalse: Boolean) : ImageRenderer {
    private var time: Long = 0
    private var first: Long = System.currentTimeMillis()

    override fun render(graphics: GuiGraphicsExtractor, x: Int, y: Int, renderWidth: Int, tickDelta: Float): Int {
        val base = "textures/config/description/$name"
        if (condition()) {
            time = System.currentTimeMillis() - first
            val height = 9 * renderWidth / 16
            graphics.blit(RenderPipelines.GUI_TEXTURED, createIdentifier("$base/${frameProvider(time)}.png"), x, y, 0f, 0f, renderWidth, height, renderWidth, height)
            return height
        } else {
            time = 0
            first = System.currentTimeMillis()
            val height = 9 * renderWidth / 16
            graphics.blit(RenderPipelines.GUI_TEXTURED, if (useFirstFrameWhenFalse) createIdentifier("$base/0.png") else createIdentifier("${base}_disabled.png"), x, y, 0f, 0f, renderWidth, height, renderWidth, height)
            return height
        }
    }

    override fun close() {
    }
}

internal class HealthRenderingImageRenderer(val healthRenderingSupplier: () -> HealthRendering) : MethodBasedImageRenderer() {
    private var first = System.currentTimeMillis()

    override fun getImagePath(): Identifier {
        val base = "textures/config/description/health_rendering"
        return when (healthRenderingSupplier()) {
            HealthRendering.ALWAYS -> createIdentifier("$base/hardcore_4.png").also {
                first = System.currentTimeMillis()
            }
            HealthRendering.TRUE -> if (((System.currentTimeMillis() - first) / 1000) % 2 == 0L) createIdentifier("$base/normal_4.png") else createIdentifier("$base/hardcore_1.png")
            HealthRendering.NEVER -> createIdentifier("$base/normal_4.png").also {
                first = System.currentTimeMillis()
            }
        }
    }
}

