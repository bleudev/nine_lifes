package com.bleudev.nine_lifes.client.render

import com.bleudev.nine_lifes.MOD_ID
import com.bleudev.nine_lifes.util.createIdentifier
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.renderpearl.api.pipeline.BlendFunction
import com.mojang.renderpearl.api.pipeline.ColorTargetState
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology
import com.mojang.renderpearl.api.pipeline.RenderPipeline
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.rendertype.RenderSetup
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.resources.Identifier
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import java.util.*

class GlowRenderer {
    /**
     * All persistent world glows.
     *
     * You can freely add/remove/change them from your client-side code.
     */
    val glowMap = hashMapOf<Identifier, Glow>()

    companion object {
        private val PIPELINE: RenderPipeline = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
                .withLocation(createIdentifier("pipeline/glow"))
                .withVertexShader(createIdentifier("core/glow"))
                .withFragmentShader(createIdentifier("core/glow"))
                .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
                .withPrimitiveTopology(PrimitiveTopology.QUADS)
                .withCull(false)
                .withColorTargetState(ColorTargetState(BlendFunction.TRANSLUCENT))
                .withDepthStencilState(Optional.empty())
                .build()
        )
        private val RENDER_TYPE: RenderType = RenderType.create(
            "$MOD_ID:glow",
            RenderSetup.builder(PIPELINE)
                .sortOnUpload()
                .createRenderSetup()
        )

        private var instance: GlowRenderer? = null

        @JvmStatic
        fun getInstance(): GlowRenderer {
            if (instance == null) {
                instance = GlowRenderer()
            }

            return instance!!
        }

        @JvmStatic
        fun reset() {
            instance = null
        }

        @JvmStatic
        fun register() {
            LevelRenderEvents.COLLECT_SUBMITS.register {
                getInstance().submitAll(it)
            }
        }
    }

    private val submissionPoseStack = PoseStack()
    private fun submitAll(context: LevelRenderContext) {
        val camera = context.levelState().cameraRenderState
        val collector = context.submitNodeCollector()
        for (glow in glowMap.values) {
            submitAt(collector, camera, glow.position, glow)
        }
    }

    fun submitAt(
        collector: SubmitNodeCollector,
        camera: CameraRenderState,
        position: Vec3,
        glow: Glow
    ) {
        if (glow.intensity <= 0f || glow.radius <= 0f) return

        val cameraPos = camera.pos
        val distance = position.distanceTo(cameraPos)
        if (distance >= glow.fadeEnd) return

        val distanceFade = smoothDistanceFade(distance, glow.fadeStart, glow.fadeEnd)
        val alpha = glow.intensity.coerceIn(0f, 1f) * distanceFade
        if (alpha <= 0f) return

        submissionPoseStack.pushPose()
        submissionPoseStack.translate(
            (position.x - cameraPos.x),
            (position.y - cameraPos.y),
            (position.z - cameraPos.z)
        )

        val right = Vector3f(1f, 0f, 0f)
        val up = Vector3f(0f, 1f, 0f)
        camera.orientation.transform(right)
        camera.orientation.transform(up)

        val state = GlowRenderState(
            glow.radius,
            right, up,
            glow.color.x, glow.color.y, glow.color.z, alpha
        )
        collector.submitCustomGeometry(submissionPoseStack, RENDER_TYPE) { pose, buffer -> drawGlow(pose, buffer, state) }
        submissionPoseStack.popPose()
    }

    fun submitAt(
        context: LevelRenderContext,
        position: Vec3,
        glow: Glow
    ) {
        submitAt(context.submitNodeCollector(), context.levelState().cameraRenderState, position, glow)
    }

    private fun drawGlow(
        pose: PoseStack.Pose,
        builder: VertexConsumer,
        state: GlowRenderState
    ) {
        val radius = state.radius
        val right = state.right
        val up = state.up
        val bl = Vector3f()
            .sub(Vector3f(right).mul(radius))
            .sub(Vector3f(up).mul(radius))
        val br = Vector3f()
            .add(Vector3f(right).mul(radius))
            .sub(Vector3f(up).mul(radius))
        val tr = Vector3f()
            .add(Vector3f(right).mul(radius))
            .add(Vector3f(up).mul(radius))
        val tl = Vector3f()
            .sub(Vector3f(right).mul(radius))
            .add(Vector3f(up).mul(radius))

        builder.addVertex(pose, bl.x, bl.y, bl.z)
            .setUv(0f, 1f)
            .setColor(state.red, state.green, state.blue, state.alpha)
        builder.addVertex(pose, br.x, br.y, br.z)
            .setUv(1f, 1f)
            .setColor(state.red, state.green, state.blue, state.alpha)
        builder.addVertex(pose, tr.x, tr.y, tr.z)
            .setUv(1f, 0f)
            .setColor(state.red, state.green, state.blue, state.alpha)
        builder.addVertex(pose, tl.x, tl.y, tl.z)
            .setUv(0f, 0f)
            .setColor(state.red, state.green, state.blue, state.alpha)
    }

    private fun smoothDistanceFade(
        distance: Double,
        start: Float,
        end: Float
    ): Float {
        if (distance <= start) return 1f
        if (distance >= end || end <= start) return 0f

        val t = ((distance - start) / (end - start)).toFloat().coerceIn(0f, 1f)
        val smooth = t * t * (3f - 2f * t)
        return 1f - smooth
    }

    data class Glow(
        var position: Vec3,
        /**
         * Radius in blocks.
         */
        var radius: Float = 2f,
        /**
         * Overall intensity.
         */
        var intensity: Float = 1f,
        /**
         * RGB in 0..1.
         */
        var color: Vector3f = Vector3f(1f, 1f, 1f),
        /**
         * Distance at which camera fading starts.
         */
        var fadeStart: Float = 32f,
        /**
         * Distance at which the glow disappears.
         */
        var fadeEnd: Float = 64f
    )

    private data class GlowRenderState(
        val radius: Float,
        val right: Vector3f,
        val up: Vector3f,
        val red: Float,
        val green: Float,
        val blue: Float,
        val alpha: Float
    )
}