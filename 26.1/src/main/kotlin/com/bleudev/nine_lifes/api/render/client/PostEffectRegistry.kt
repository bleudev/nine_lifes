package com.bleudev.nine_lifes.api.render.client

import com.bleudev.nine_lifes.api.EmptyPredicate
import com.bleudev.nine_lifes.util.createIdentifier
import net.minecraft.resources.Identifier

interface PostEffectRegistry {
    companion object {
        fun register(vararg effects: Pair<Identifier, EmptyPredicate>) {
            for ((id, pr) in effects) {
                PostEffectRegistryImpl.register(id, pr)
            }
        }
        fun register(vararg identifiers: Identifier) = register(*identifiers.map { it to {true} }.toTypedArray())
        @Suppress("unused") // Public API
        fun registerDefault(path: String) = register(Identifier.withDefaultNamespace(path))
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