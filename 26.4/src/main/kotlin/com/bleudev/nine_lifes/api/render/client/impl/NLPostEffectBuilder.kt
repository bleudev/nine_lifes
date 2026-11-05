package com.bleudev.nine_lifes.api.render.client.impl

import com.bleudev.nine_lifes.api.Condition
import com.bleudev.nine_lifes.api.render.client.UniformBuilder
import com.bleudev.nine_lifes.api.render.client.post_effect.NLPostEffect
import net.minecraft.resources.Identifier

abstract class NLPostEffectBuilderImpl<T : NLPostEffect>(protected val postEffect: Identifier) {
    protected var condition: Condition = {true}
    protected val uniformNames: ArrayList<String> = arrayListOf()
    protected val uniforms: HashMap<String, UniformBuilder.() -> Unit> = hashMapOf()

    fun condition(condition: Condition): NLPostEffectBuilderImpl<T> {
        this.condition = condition
        return this
    }

    fun uniform(name: String, builder: UniformBuilder.() -> Unit): NLPostEffectBuilderImpl<T> {
        this.uniformNames.add(name)
        this.uniforms[name] = builder
        return this
    }

    protected abstract fun build(uniformExtractor: (name: String, builder: UniformBuilder) -> Unit): T

    fun build(): T {
        return build { name, builder -> this.uniforms[name]!!(builder) }
    }
}