package com.bleudev.nine_lifes.api.render.client

import com.mojang.renderpearl.api.buffers.GpuBuffer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.MappableRingBuffer
import net.minecraft.resources.Identifier

@Environment(EnvType.CLIENT)
interface DynamicUniformsRegistry {
    data class Context(val uniformName: String, val shadersIds: List<Identifier>? = null) {
        constructor(uniformName: String, vararg shader: Identifier) : this(uniformName, shader.toList().ifEmpty { null })
    }

    typealias UniformBuilderTransformer = PostEffectRegistry.Builder.UniformBuilder.() -> Unit

    companion object {
        /**
         * Register dynamic uniform.
         *
         *
         * Example:
         * ```kotlin
         * DynamicUniformsRegistry.register(DynamicUniformsRegistry.Context("Config", Identifier.fromNamespaceAndPath("test", "shader")), {putVec3()}) {
         *   putVec3(1f, 0f, 0f)
         * }
         * ```
         *
         * @param context Uniform context (name and (optionaly) shaders ids)
         * @param transformer Uniform transformer
         * */
        fun register(context: Context, transformer: UniformBuilderTransformer) {
            DynamicUniformsRegistryImpl.register(context, transformer)
        }
    }
}

object DynamicUniformsRegistryImpl {
    private val BUFFER_QUERY: HashMap<DynamicUniformsRegistry.Context, () -> MappableRingBuffer> = HashMap()
    private val BUFFERS: HashMap<DynamicUniformsRegistry.Context, MappableRingBuffer> = HashMap()
    private val TRANSFORMERS: HashMap<DynamicUniformsRegistry.Context, DynamicUniformsRegistry.UniformBuilderTransformer> = HashMap()

    internal fun register(context: DynamicUniformsRegistry.Context, transformer: DynamicUniformsRegistry.UniformBuilderTransformer) {
        val b = PostEffectRegistry.Builder.UniformBuilder()
        b.transformer()
        BUFFER_QUERY[context] = { MappableRingBuffer( { "${context.uniformName} UBO" }, 130, b.size()) }
        TRANSFORMERS[context] = transformer
    }

    internal fun initBuffers() {
        BUFFERS.clear()
        for (entry in BUFFER_QUERY) {
            BUFFERS[entry.key] = entry.value()
        }
    }

    internal fun updateBuffers() {
        for (entry in BUFFERS) {
            if (entry.value.currentBuffer().isClosed) {
                initBuffers()
                return
            }
            entry.value.currentBuffer().map(false, true).use { view ->
                view.data().position(0)
                TRANSFORMERS[entry.key]!!(PostEffectRegistry.Builder.UniformBuilder(view.data()))
            }
        }
    }

    @JvmStatic
    internal fun getNewUniforms(current: MutableMap<String, GpuBuffer>, shaderId: Identifier): MutableMap<String, GpuBuffer> {
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