package com.bleudev.nine_lifes.api.render.client

import com.bleudev.nine_lifes.api.EmptyPredicate
import com.bleudev.nine_lifes.util.createIdentifier
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.resources.Identifier

@Environment(EnvType.CLIENT)
interface PostEffectRegistry {
    companion object {
        /**
         * Register post effects to render.
         *
         * Example:
         * ```kotlin
         * PostEffectRegistry.register(Identifier.fromNamespaceAndPath("test", "shader") to {shouldRender})
         * ```
         *
         * @param effects post effects infos. First element in pair: post effect identifier, second element: post effect predicate (should post effect render this time?).
         * */
        fun register(vararg effects: Pair<Identifier, EmptyPredicate>) {
            for ((id, pr) in effects) {
                PostEffectRegistryImpl.register(id, pr)
            }
        }
        /**
         * Register post effects to render without predicates.
         *
         * Example:
         * ```kotlin
         * PostEffectRegistry.register(Identifier.fromNamespaceAndPath("test", "shader"))
         * ```
         *
         * @param identifiers Post effects identifiers. All post effects will render every frame.
         * */
        fun register(vararg identifiers: Identifier) = register(*identifiers.map { it to {true} }.toTypedArray())
        /**
         * Register post effects with default namespace ("minecraft").
         *
         * Example:
         * ```kotlin
         * PostEffectRegistry.registerDefault("shader")
         * ```
         *
         * @param paths Post effects paths without namespace.
         * */
        @Suppress("unused") // Public API
        fun registerDefault(vararg paths: String) = register(*paths.map(Identifier::withDefaultNamespace).toTypedArray())

        internal fun registerNineLifes(vararg paths: String) {
            register(*paths.map(::createIdentifier).toTypedArray())
        }
    }
}

object PostEffectRegistryImpl {
    private val POST_EFFECTS = HashMap<Identifier, EmptyPredicate>()

    internal fun register(identifier: Identifier, predicate: EmptyPredicate) {
        POST_EFFECTS[identifier] = predicate
    }

    internal fun execute(renderer: (Identifier) -> Unit) {
        for ((id, pr) in POST_EFFECTS) {
            if (pr()) {
                renderer(id)
            }
        }
    }
}