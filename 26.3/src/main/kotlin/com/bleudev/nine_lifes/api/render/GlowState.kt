package com.bleudev.nine_lifes.api.render

import com.bleudev.nine_lifes.api.FriendlyStreamCodec
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import org.joml.Vector3fc

data class GlowState(
    /**
     * Glow position.
     * Use [Vec3.ZERO] if this is entity glow state.
     * */
    var position: Vec3 = Vec3.ZERO,
    /**
     * Radius in blocks.
     */
    var radius: Float = 1.5f,
    /**
     * Overall intensity.
     */
    var intensity: Float = 1f,
    /**
     * RGB in 0..1.
     */
    var color: Vector3fc = Vector3f(1f, 1f, 1f),
    /**
     * Distance at which camera fading starts.
     */
    var fadeStart: Float = 32f,
    /**
     * Distance at which the glow disappears.
     */
    var fadeEnd: Float = 64f
) {
    companion object {
        val STREAM_CODEC: FriendlyStreamCodec<GlowState> = StreamCodec.composite(
            Vec3.STREAM_CODEC, GlowState::position,
            ByteBufCodecs.FLOAT, GlowState::radius,
            ByteBufCodecs.FLOAT, GlowState::intensity,
            ByteBufCodecs.VECTOR3F, GlowState::color,
            ByteBufCodecs.FLOAT, GlowState::fadeStart,
            ByteBufCodecs.FLOAT, GlowState::fadeEnd,
            ::GlowState
        )
    }
}