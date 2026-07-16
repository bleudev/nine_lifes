package com.bleudev.nine_lifes.api.render.client

import com.bleudev.nine_lifes.api.EmptyPredicate
import com.bleudev.nine_lifes.util.createIdentifier
import com.mojang.blaze3d.buffers.Std140Builder
import com.mojang.blaze3d.buffers.Std140SizeCalculator
import com.mojang.renderpearl.api.buffers.GpuBuffer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.MappableRingBuffer
import net.minecraft.resources.Identifier
import org.joml.*
import java.nio.ByteBuffer

@Environment(EnvType.CLIENT)
interface PostEffectRegistry {
    typealias UniformBuilderTransformer = PostEffectRegistry.Builder.UniformBuilder.() -> Unit

    fun register(identifier: Identifier, renderPredicate: EmptyPredicate): Builder
    fun register(identifier: Identifier): Builder = register(identifier) { true }

    companion object {
        private val POST_EFFECT_REGISTRY = PostEffectRegistryImpl()

        /**
         * Register post effect to render.
         *
         * Example:
         * ```kotlin
         * PostEffectRegistry.register(Identifier.fromNamespaceAndPath("test", "shader") to {shouldRender})
         * ```
         *
         * @param identifier Post effect identifier
         * @param renderPredicate Post effect predicate (should post effect render this time?)
         * */
        fun register(identifier: Identifier, renderPredicate: EmptyPredicate): Builder =
            POST_EFFECT_REGISTRY.register(identifier, renderPredicate)
        /**
         * Register post effect to render without predicate.
         *
         * Example:
         * ```kotlin
         * PostEffectRegistry.register(Identifier.fromNamespaceAndPath("test", "shader"))
         * ```
         *
         * @param identifier Post effect identifier. It will render every frame.
         * */
        fun register(identifier: Identifier): Builder =
            POST_EFFECT_REGISTRY.register(identifier)
        /**
         * Register post effect with default namespace ("minecraft").
         *
         * Example:
         * ```kotlin
         * PostEffectRegistry.registerDefault("creeper")
         * ```
         *
         * @param path Post effect path without namespace.
         * */
        @Suppress("unused") // Public API
        fun registerDefault(path: String): Builder =
            register(Identifier.withDefaultNamespace(path))
        internal fun registerNineLifes(path: String): Builder =
            register(createIdentifier(path))

        internal fun execute(renderer: (Identifier) -> Unit) {
            UniformRegistryImpl.updateBuffers()
            POST_EFFECT_REGISTRY.execute(renderer)
        }
        internal fun initBuffers() = UniformRegistryImpl.initBuffers()
        internal fun getNewUniforms(current: MutableMap<String, GpuBuffer>, shaderId: Identifier): MutableMap<String, GpuBuffer> =
            UniformRegistryImpl.getNewUniforms(current, shaderId)
    }

    interface Builder {
        @Suppress("unused")
        class UniformBuilder(buffer: ByteBuffer? = null) {
            private var sizeCalculator = Std140SizeCalculator()
            private val builder = buffer?.let { Std140Builder.intoBuffer(it) }

            fun putFloat(value: Float): UniformBuilder {
                sizeCalculator.putFloat()
                builder?.putFloat(value)
                return this
            }

            fun putInt(value: Int): UniformBuilder {
                sizeCalculator.putInt()
                builder?.putInt(value)
                return this
            }

            fun putVec2(x: Float, y: Float): UniformBuilder {
                sizeCalculator.putVec2()
                builder?.putVec2(x, y)
                return this
            }

            fun putVec2(vec: Vector2fc): UniformBuilder {
                sizeCalculator.putVec2()
                builder?.putVec2(vec)
                return this
            }

            fun putIVec2(x: Int, y: Int): UniformBuilder {
                sizeCalculator.putIVec2()
                builder?.putIVec2(x, y)
                return this
            }

            fun putIVec2(vec: Vector2ic): UniformBuilder {
                sizeCalculator.putIVec2()
                builder?.putIVec2(vec)
                return this
            }

            fun putVec3(x: Float, y: Float, z: Float): UniformBuilder {
                sizeCalculator.putVec3()
                builder?.putVec3(x, y, z)
                return this
            }

            fun putVec3(vec: Vector3fc): UniformBuilder {
                sizeCalculator.putVec3()
                builder?.putVec3(vec)
                return this
            }

            fun putIVec3(x: Int, y: Int, z: Int): UniformBuilder {
                sizeCalculator.putIVec3()
                builder?.putIVec3(x, y, z)
                return this
            }

            fun putIVec3(vec: Vector3ic): UniformBuilder {
                sizeCalculator.putIVec3()
                builder?.putIVec3(vec)
                return this
            }

            fun putVec4(x: Float, y: Float, z: Float, w: Float): UniformBuilder {
                sizeCalculator.putVec4()
                builder?.putVec4(x, y, z, w)
                return this
            }

            fun putVec4(vec: Vector4fc): UniformBuilder {
                sizeCalculator.putVec4()
                builder?.putVec4(vec)
                return this
            }

            fun putIVec4(x: Int, y: Int, z: Int, w: Int): UniformBuilder {
                sizeCalculator.putIVec4()
                builder?.putIVec4(x, y, z, w)
                return this
            }

            fun putIVec4(vec: Vector4ic): UniformBuilder {
                sizeCalculator.putIVec4()
                builder?.putIVec4(vec)
                return this
            }

            fun putMat4f(vec: Matrix4fc): UniformBuilder {
                sizeCalculator.putMat4f()
                builder?.putMat4f(vec)
                return this
            }

            fun size(): Int = sizeCalculator.get()
        }

        fun uniform(
            name: String,
            transformer: UniformBuilder.() -> Unit
        ): Builder
    }
}

private class PostEffectRegistryImpl : PostEffectRegistry {
    private val POST_EFFECTS = HashMap<Identifier, EmptyPredicate>()

    override fun register(identifier: Identifier, renderPredicate: EmptyPredicate): PostEffectRegistry.Builder {
        POST_EFFECTS[identifier] = renderPredicate
        return BuilderImpl(identifier)
    }

    fun execute(renderer: (Identifier) -> Unit) {
        for ((id, pr) in POST_EFFECTS) {
            if (pr()) {
                renderer(id)
            }
        }
    }

    private class BuilderImpl(val postEffectIdentifier: Identifier) : PostEffectRegistry.Builder {
        override fun uniform(
            name: String,
            transformer: PostEffectRegistry.Builder.UniformBuilder.() -> Unit
        ): PostEffectRegistry.Builder {
            UniformRegistryImpl.register(
                PostEffectContext(name, postEffectIdentifier),
                transformer
            )
            return this
        }
    }
}

private object UniformRegistryImpl {
    private val BUFFER_QUERY: HashMap<PostEffectContext, () -> MappableRingBuffer> = HashMap()
    private val BUFFERS: HashMap<PostEffectContext, MappableRingBuffer> = HashMap()
    private val TRANSFORMERS: HashMap<PostEffectContext, PostEffectRegistry.UniformBuilderTransformer> = HashMap()

    fun register(postEffectContext: PostEffectContext, transformer: PostEffectRegistry.UniformBuilderTransformer) {
        val b = PostEffectRegistry.Builder.UniformBuilder()
        b.transformer()
        BUFFER_QUERY[postEffectContext] = { MappableRingBuffer( { "${postEffectContext.uniformName} UBO" }, 130, b.size()) }
        TRANSFORMERS[postEffectContext] = transformer
    }

    fun initBuffers() {
        BUFFERS.clear()
        for (entry in BUFFER_QUERY) {
            BUFFERS[entry.key] = entry.value()
        }
    }

    fun updateBuffers() {
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

    fun getNewUniforms(current: MutableMap<String, GpuBuffer>, shaderId: Identifier): MutableMap<String, GpuBuffer> {
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

private data class PostEffectContext(val uniformName: String, val shadersIds: List<Identifier>? = null) {
    constructor(uniformName: String, vararg shader: Identifier) : this(uniformName, shader.toList().ifEmpty { null })
}
