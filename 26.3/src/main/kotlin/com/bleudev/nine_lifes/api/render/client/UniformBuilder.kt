package com.bleudev.nine_lifes.api.render.client

import com.mojang.blaze3d.buffers.Std140Builder
import com.mojang.blaze3d.buffers.Std140SizeCalculator
import org.joml.*
import java.nio.ByteBuffer

@Suppress("unused")
class UniformBuilder(buffer: ByteBuffer? = null) {
    private var sizeCalculator = Std140SizeCalculator()
    private val builder = buffer?.let { Std140Builder.intoBuffer(it) }

    fun putFloat(value: Float): UniformBuilder {
        sizeCalculator.putFloat()
        builder?.putFloat(value)
        return this
    }

    fun putInt(value: Int): UniformBuilder {
        sizeCalculator.putInt()
        builder?.putInt(value)
        return this
    }

    fun putVec2(x: Float, y: Float): UniformBuilder {
        sizeCalculator.putVec2()
        builder?.putVec2(x, y)
        return this
    }

    fun putVec2(vec: Vector2fc): UniformBuilder {
        sizeCalculator.putVec2()
        builder?.putVec2(vec)
        return this
    }

    fun putIVec2(x: Int, y: Int): UniformBuilder {
        sizeCalculator.putIVec2()
        builder?.putIVec2(x, y)
        return this
    }

    fun putIVec2(vec: Vector2ic): UniformBuilder {
        sizeCalculator.putIVec2()
        builder?.putIVec2(vec)
        return this
    }

    fun putVec3(x: Float, y: Float, z: Float): UniformBuilder {
        sizeCalculator.putVec3()
        builder?.putVec3(x, y, z)
        return this
    }

    fun putVec3(vec: Vector3fc): UniformBuilder {
        sizeCalculator.putVec3()
        builder?.putVec3(vec)
        return this
    }

    fun putIVec3(x: Int, y: Int, z: Int): UniformBuilder {
        sizeCalculator.putIVec3()
        builder?.putIVec3(x, y, z)
        return this
    }

    fun putIVec3(vec: Vector3ic): UniformBuilder {
        sizeCalculator.putIVec3()
        builder?.putIVec3(vec)
        return this
    }

    fun putVec4(x: Float, y: Float, z: Float, w: Float): UniformBuilder {
        sizeCalculator.putVec4()
        builder?.putVec4(x, y, z, w)
        return this
    }

    fun putVec4(vec: Vector4fc): UniformBuilder {
        sizeCalculator.putVec4()
        builder?.putVec4(vec)
        return this
    }

    fun putIVec4(x: Int, y: Int, z: Int, w: Int): UniformBuilder {
        sizeCalculator.putIVec4()
        builder?.putIVec4(x, y, z, w)
        return this
    }

    fun putIVec4(vec: Vector4ic): UniformBuilder {
        sizeCalculator.putIVec4()
        builder?.putIVec4(vec)
        return this
    }

    fun putMat4f(vec: Matrix4fc): UniformBuilder {
        sizeCalculator.putMat4f()
        builder?.putMat4f(vec)
        return this
    }

    private var size: Int? = null
    fun size(): Int {
        // Avoid changing size after first get
        this.size = this.size ?: sizeCalculator.get()
        return this.size!!
    }
}