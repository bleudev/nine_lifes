package com.bleudev.nine_lifes.client.api.render

import com.bleudev.nine_lifes.client.api.render.DynamicUniformsRegistry.DynamicUniformTransformer
import com.mojang.blaze3d.buffers.GpuBuffer
import com.mojang.blaze3d.buffers.Std140Builder
import com.mojang.blaze3d.buffers.Std140SizeCalculator
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.renderer.MappableRingBuffer
import net.minecraft.resources.ResourceLocation

interface DynamicUniformsRegistry {
    data class Context(val uniformName: String, val shadersIds: List<ResourceLocation>? = null) {
        constructor(uniformName: String, vararg shader: ResourceLocation) : this(uniformName, shader.toList().ifEmpty { null })
    }

    typealias UniformSizeTransformer = Std140SizeCalculator.() -> Std140SizeCalculator
    typealias DynamicUniformTransformer = Std140Builder.() -> Unit

    companion object {
        fun register(context: Context, sizeTransformer: UniformSizeTransformer, transformer: DynamicUniformTransformer) {
            DynamicUniformsRegistryImpl.register(context, Std140SizeCalculator().sizeTransformer().get(), transformer)
        }
    }
}

object DynamicUniformsRegistryImpl {
    private val BUFFER_QUERY: HashMap<DynamicUniformsRegistry.Context, () -> MappableRingBuffer> = HashMap()
    private val BUFFERS: HashMap<DynamicUniformsRegistry.Context, MappableRingBuffer> = HashMap()
    private val TRANSFORMERS: HashMap<DynamicUniformsRegistry.Context, DynamicUniformTransformer> = HashMap()

    internal fun register(context: DynamicUniformsRegistry.Context, size: Int, transformer: DynamicUniformTransformer) {
        BUFFER_QUERY[context] = { MappableRingBuffer( { "${context.uniformName} UBO" }, 130, size) }
        TRANSFORMERS[context] = transformer
    }

    internal fun initBuffers() {
        for (entry in BUFFER_QUERY) {
            BUFFERS[entry.key] = entry.value()
        }
        BUFFER_QUERY.clear()
    }

    internal fun updateBuffers() {
        val encoder = RenderSystem.getDevice().createCommandEncoder()
        for (entry in BUFFERS) {
            encoder.mapBuffer(entry.value.currentBuffer(), false, true).use { view ->
                view.data().position(0)
                TRANSFORMERS[entry.key]!!(Std140Builder.intoBuffer(view.data()))
            }
        }
    }

    @JvmStatic
    internal fun getNewUniforms(current: MutableMap<String, GpuBuffer>, shaderId: ResourceLocation): MutableMap<String, GpuBuffer> {
        for (entry in BUFFERS) {
            val ids = entry.key.shadersIds
            if (ids != null && shaderId !in ids) continue
            if (current.containsKey(entry.key.uniformName)) {
                current[entry.key.uniformName] = entry.value.currentBuffer()
            }
        }
        return current
    }
}