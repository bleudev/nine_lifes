package com.bleudev.nine_lifes.api.render.client.post_effect

import com.bleudev.nine_lifes.api.render.client.UniformBuilder
import com.bleudev.nine_lifes.api.render.client.impl.NLPostEffectBuilderImpl
import com.bleudev.nine_lifes.util.createIdentifier
import net.minecraft.resources.Identifier

abstract class NLPostEffect(val id: Identifier, val uniformNames: List<String>) {
    abstract fun shouldRender(): Boolean
    abstract fun extractUniform(name: String, builder: UniformBuilder)

            class Builder internal constructor(postEffect: Identifier) : NLPostEffectBuilderImpl<NLPostEffect>(postEffect) {
                override fun build(uniformExtractor: (name: String, builder: UniformBuilder) -> Unit): NLPostEffect {
                    return object : NLPostEffect(this@Builder.postEffect, this@Builder.uniformNames) {
                        override fun shouldRender(): Boolean = this@Builder.condition()
                        override fun extractUniform(name: String, builder: UniformBuilder) = uniformExtractor(name, builder)
                    }
                }
            }

    companion object {
        /**
         * Build post effect.
         *
         * Example:
         * ```kotlin
         * NLPostEffect.builder(Identifier.fromNamespaceAndPath("test", "shader"))
         * ```
         *
         * @param id Post effect identifier
         * @return Post effect's builder
         * */
        fun builder(id: Identifier): Builder {
            return Builder(id)
        }

        /**
         * Build post effect with default namespace ("minecraft").
         *
         * Example:
         * ```kotlin
         * NLPostEffect.builderDefault("creeper")
         * ```
         *
         * @param postEffect Post effect path without namespace.
         * @return Post effect's builder
         * */
        @Suppress("unused") // Public API
        fun builderDefault(postEffect: String): Builder {
            return Builder(Identifier.withDefaultNamespace(postEffect))
        }

        internal fun builderNL(postEffect: String): Builder {
            return Builder(createIdentifier(postEffect))
        }
    }
}