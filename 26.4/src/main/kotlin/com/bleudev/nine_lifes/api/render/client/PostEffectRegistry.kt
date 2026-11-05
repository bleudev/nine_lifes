package com.bleudev.nine_lifes.api.render.client

import com.bleudev.nine_lifes.api.render.client.impl.PostEffectRegistryImpl
import com.bleudev.nine_lifes.api.render.client.post_effect.NLPostEffect
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Environment(EnvType.CLIENT)
interface PostEffectRegistry {
    fun register(postEffect: NLPostEffect)

    companion object {
        /**
         * Register post effect to render.
         *
         * Example:
         * ```kotlin
         * // Render `foo:bar` post effect in the nether
         * PostEffectRegistry.register(NLPostEffect
         *     .builder(Identifier.fromNamespaceAndPath("foo", "bar"))
         *     .condition { Minecraft.getInstance().level?.dimension() == Level.NETHER }
         *     .uniform("AnaglyphConfig") {
         *         putVec2(0.005f, 0.0025f)
         *     }
         *     .build()
         * )
         * ```
         *
         * @param postEffect Post effect to render
         * */
        fun register(postEffect: NLPostEffect) {
            PostEffectRegistryImpl.register(postEffect)
        }
    }
}