package com.bleudev.nine_lifes.api.render.client

import com.bleudev.nine_lifes.api.EmptyPredicate
import com.bleudev.nine_lifes.util.createIdentifier
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.resources.Identifier

@Environment(EnvType.CLIENT)
interface PostEffectRegistry {
    fun register(identifier: Identifier, renderPredicate: EmptyPredicate): Builder
    fun register(identifier: Identifier): Builder = register(identifier) { true }

    companion object {
        private val INSTANCE = PostEffectRegistryImpl()

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
            INSTANCE.register(identifier, renderPredicate)
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
            INSTANCE.register(identifier)
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

        internal fun execute(renderer: (Identifier) -> Unit) =
            INSTANCE.execute(renderer)
    }

    interface Builder {
        fun uniform(
            name: String, sizeTransformer: DynamicUniformsRegistry.UniformSizeTransformer,
            transformer: DynamicUniformsRegistry.DynamicUniformTransformer
        ): Builder
    }
}

private class PostEffectRegistryImpl : PostEffectRegistry {
    private val POST_EFFECTS = HashMap<Identifier, EmptyPredicate>()

    override fun register(identifier: Identifier, predicate: EmptyPredicate): PostEffectRegistry.Builder {
        POST_EFFECTS[identifier] = predicate
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
            sizeTransformer: DynamicUniformsRegistry.UniformSizeTransformer,
            transformer: DynamicUniformsRegistry.DynamicUniformTransformer
        ): PostEffectRegistry.Builder {
            DynamicUniformsRegistry.register(
                DynamicUniformsRegistry.Context(name, postEffectIdentifier),
                sizeTransformer,
                transformer
            )
            return this
        }
    }
}