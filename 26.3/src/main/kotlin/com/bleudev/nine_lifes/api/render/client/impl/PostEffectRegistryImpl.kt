package com.bleudev.nine_lifes.api.render.client.impl

import com.bleudev.nine_lifes.api.render.client.PostEffectRegistry
import com.bleudev.nine_lifes.api.render.client.UniformBuilder
import com.bleudev.nine_lifes.api.render.client.post_effect.NLPostEffect
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.renderpearl.api.buffers.GpuBuffer
import net.minecraft.client.renderer.MappableRingBuffer
import net.minecraft.resources.Identifier

internal object PostEffectRegistryImpl : PostEffectRegistry {
    private val postEffects = arrayListOf<NLPostEffect>()
    private val bufferQueries = hashMapOf<Identifier, ArrayList<MyBufferQuery>>()
    private val buffers = hashMapOf<Identifier, List<MyBuffer>>()
    private var initialized = false

    override fun register(postEffect: NLPostEffect) {
        require(!initialized) { "Can't register post effect after initialization!" }
        this.postEffects.add(postEffect)
        for (uniform in postEffect.uniformNames) {
            val list = this.bufferQueries.computeIfAbsent(postEffect.id) { arrayListOf<MyBufferQuery>() }
            list.add(MyBufferQuery.create(uniform, postEffect))
            this.bufferQueries[postEffect.id] = list
        }
    }

    private fun init() {
        if (!initialized) {
            requireNotNull(RenderSystem.tryGetDevice()) { "Can't init buffers before device being initialized!" }
            reload()
            initialized = true
        }
    }

    fun reload() {
        this.buffers.clear()
        for ((id, queries) in this.bufferQueries) {
            this.buffers[id] = queries.map { it.init() }
        }
    }

    fun update() {
        for (postEffect in this.postEffects) {
            val bufs = this.buffers[postEffect.id]
            if (bufs == null || bufs.any { it.buffer.currentBuffer().isClosed }) {
                reload()
                return
            }
            for ((_, name, buf) in bufs) {
                buf.currentBuffer().map(false, true).use { view ->
                    view.data().position(0)
                    val b = UniformBuilder(view.data())
                    postEffect.extractUniform(name, b)
                }
            }
        }
    }

    @JvmStatic
    fun extractPostEffects(): List<Identifier> {
        return this.postEffects.toList().filter { it.shouldRender() }.map { it.id }
    }

    @JvmStatic
    fun extractBuffers(postEffect: Identifier): Map<String, GpuBuffer> {
        if (!initialized) init()
        update()
        return this.buffers[postEffect]?.associate { it.uniformName to it.buffer.currentBuffer() } ?: mapOf()
    }

    @JvmStatic
    fun close() {
        for (bufs in this.buffers.values.toList()) {
            for ((_, _, buf) in bufs) {
                buf.close()
            }
        }
        this.postEffects.clear()
        this.buffers.clear()
    }

    data class MyBufferQuery(val postEffect: NLPostEffect, val uniformName: String, val bufferSupplier: () -> MappableRingBuffer) {
        fun init(): MyBuffer {
            return MyBuffer(postEffect, uniformName, bufferSupplier())
        }

        companion object {
            fun create(uniformName: String, postEffect: NLPostEffect): MyBufferQuery {
                val b = UniformBuilder()
                postEffect.extractUniform(uniformName, b)
                return MyBufferQuery(postEffect, uniformName) {
                    MappableRingBuffer( { "${postEffect.id}/$uniformName UBO" }, 130, b.size())
                }
            }
        }
    }
    data class MyBuffer(val postEffect: NLPostEffect, val uniformName: String, val buffer: MappableRingBuffer)
}