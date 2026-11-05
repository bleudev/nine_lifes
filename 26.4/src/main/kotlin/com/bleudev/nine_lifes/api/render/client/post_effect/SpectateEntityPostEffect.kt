package com.bleudev.nine_lifes.api.render.client.post_effect

import com.bleudev.nine_lifes.api.render.client.UniformBuilder
import com.bleudev.nine_lifes.api.render.client.impl.NLPostEffectBuilderImpl
import com.bleudev.nine_lifes.util.createIdentifier
import net.minecraft.client.CameraType
import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

abstract class SpectateEntityPostEffect(id: Identifier, uniformNames: List<String>, protected val entityPredicate: (Entity) -> Boolean) : NLPostEffect(id, uniformNames) {
    override fun shouldRender(): Boolean {
        val mc = Minecraft.getInstance()
        val entity = mc.cameraEntity
        return mc.options.cameraType == CameraType.FIRST_PERSON && entity != null && entityPredicate(entity)
    }

    class Builder internal constructor(postEffect: Identifier, private val entityPredicate: (Entity) -> Boolean) : NLPostEffectBuilderImpl<SpectateEntityPostEffect>(postEffect) {
        override fun build(uniformExtractor: (name: String, builder: UniformBuilder) -> Unit): SpectateEntityPostEffect {
            return object : SpectateEntityPostEffect(this@Builder.postEffect, this@Builder.uniformNames, this@Builder.entityPredicate) {
                override fun shouldRender(): Boolean = super.shouldRender() && this@Builder.condition()
                override fun extractUniform(name: String, builder: UniformBuilder) = uniformExtractor(name, builder)
            }
        }
    }

    companion object {
        fun builder(postEffect: Identifier, entityPredicate: (Entity) -> Boolean): Builder {
            return Builder(postEffect, entityPredicate)
        }
        fun <T : Entity> builder(postEffect: Identifier, entityType: EntityType<T>): Builder {
            return builder(postEffect) { it.type == entityType }
        }

        internal fun <T : Entity> builderNL(postEffect: String, entityType: EntityType<T>): Builder {
            return builder(createIdentifier(postEffect), entityType)
        }
    }
}