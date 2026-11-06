package com.bleudev.nine_lifes.api.render.client.impl

import com.bleudev.nine_lifes.LOGGER
import com.bleudev.nine_lifes.api.render.client.PostEffectRegistry
import com.bleudev.nine_lifes.api.render.client.UniformBuilder
import com.bleudev.nine_lifes.api.render.client.post_effect.NLPostEffect
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.renderpearl.api.buffers.GpuBuffer
import net.minecraft.client.renderer.MappableRingBuffer
import net.minecraft.resources.Identifier

internal object PostEffectRegistryImpl : PostEffectRegistry {
    private val postEffects = arrayListOf<NLPostEffect>()
    private val postEffectsById = hashMapOf<Identifier, NLPostEffect>()

    private val bufferQueries = hashMapOf<Identifier, ArrayList<MyBufferQuery>>()
    private val buffers = hashMapOf<Identifier, List<MyBuffer>>()
    private val bufCache = hashMapOf<Identifier, HashMap<String, GpuBuffer>>()
    private var initialized = false

    override fun register(postEffect: NLPostEffect) {
        require(!initialized) { "Can't register post effect after initialization!" }
        this.postEffects.add(postEffect)
        this.postEffectsById[postEffect.id] = postEffect
        for (uniform in postEffect.uniformNames) {
            val list = this.bufferQueries.computeIfAbsent(postEffect.id) { arrayListOf<MyBufferQuery>() }
            list.add(MyBufferQuery.create(uniform, postEffect))
            this.bufferQueries[postEffect.id] = list
        }
    }

    /** Try initialization and update buffers **/
    fun frameStart() {
        if (tryInit()) {
            update()
        }
    }

    private fun tryInit(): Boolean {
        if (!initialized && RenderSystem.tryGetDevice() != null) {
            reload()
            initialized = true
        }
        return initialized
    }

    private fun reload() {
        this.buffers.clear()
        for ((id, queries) in this.bufferQueries) {
            this.buffers[id] = queries.map { it.init() }
        }
    }

    private fun update() {
        for (postEffect in this.postEffects) {
            // Skip update for post effects which aren't rendered
            if (!postEffect.shouldRender()) {
                // Ensure post effect isn't in cache
                this.bufCache.remove(postEffect.id)
                continue
            }

            val bufs = this.buffers[postEffect.id]
            if (bufs == null || bufs.any { it.buffer.currentBuffer().isClosed }) {
                reload()
                return
            }
            val cache = this.bufCache.computeIfAbsent(postEffect.id) { hashMapOf() }
            for ((_, name, buf) in bufs) {
                buf.currentBuffer().map(false, true).use { view ->
                    view.data().position(0)
                    val b = UniformBuilder(view.data())
                    postEffect.extractUniform(name, b)
                }
                cache[name] = buf.currentBuffer()
            }
        }
    }

    @JvmStatic
    fun extractPostEffects(): List<Identifier> {
        return this.postEffects.mapNotNull { if (it.shouldRender()) it.id else null }
    }

    @JvmStatic
    fun shouldApplyBuffers(postEffect: Identifier): Boolean {
        val pe = this.postEffectsById[postEffect]
        return pe != null && pe.shouldRender() && this.bufCache[postEffect] != null
    }

    @JvmStatic
    fun extractBuffers(postEffect: Identifier): Map<String, GpuBuffer> {
        if (!initialized) {
            LOGGER.error("Buffers aren't initialized yet. Cannot do extraction. Report about it.")
            return mapOf()
        }
        return this.bufCache[postEffect] ?: mapOf()
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