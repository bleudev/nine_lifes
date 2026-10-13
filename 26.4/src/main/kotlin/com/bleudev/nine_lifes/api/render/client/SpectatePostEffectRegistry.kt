package com.bleudev.nine_lifes.api.render.client

import net.minecraft.resources.Identifier
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

interface SpectatePostEffectRegistry {
    fun register(shouldApply: (Entity) -> Boolean, postEffect: Identifier)
    fun <T : Entity> register(type: EntityType<T>, postEffect: Identifier) {
        register({ entity -> entity.type == type}, postEffect)
    }

    companion object {
        fun register(shouldApply: (Entity) -> Boolean, postEffect: Identifier) {
            SpectatePostEffectRegistryImpl.register(shouldApply, postEffect)
        }
        fun <T : Entity> register(type: EntityType<T>, postEffect: Identifier): PostEffectRegistry.Builder {
            SpectatePostEffectRegistryImpl.register(type, postEffect)
            return UniformBuilderImpl(postEffect)
        }

        internal fun visit(entity: Entity?): Identifier? {
            return SpectatePostEffectRegistryImpl.visit(entity)
        }
    }
}

private object SpectatePostEffectRegistryImpl : SpectatePostEffectRegistry {
    private val predicateMap = hashMapOf<(Entity) -> Boolean, Identifier>()

    override fun register(shouldApply: (Entity) -> Boolean, postEffect: Identifier) {
        predicateMap[shouldApply] = postEffect
    }

    fun visit(entity: Entity?): Identifier? {
        if (entity == null) return null
        for ((p, e) in predicateMap) {
            if (p(entity)) {
                return e
            }
        }
        return null
    }
}