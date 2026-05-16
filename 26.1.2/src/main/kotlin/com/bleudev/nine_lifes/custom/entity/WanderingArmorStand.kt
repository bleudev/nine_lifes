package com.bleudev.nine_lifes.custom.entity

import com.bleudev.nine_lifes.WSTAND_KICK_TICKS
import com.bleudev.nine_lifes.WSTAND_KICK_TIMES
import com.bleudev.nine_lifes.WSTAND_WANDER_TICKS
import com.bleudev.nine_lifes.custom.NineLifesSounds
import com.bleudev.nine_lifes.custom.entity.ai.goal.WanderingArmorStandLookAtPlayerGoal
import com.bleudev.nine_lifes.custom.entity.ai.goal.WanderingArmorStandRandomLookAroundGoal
import com.bleudev.nine_lifes.custom.entity.ai.goal.WanderingArmorStandWaterAvoidingRandomStrollGoal
import com.bleudev.nine_lifes.custom.packet.payload.ArmorStandHitEvent
import com.bleudev.nine_lifes.util.consumeOneItemInHand
import com.bleudev.nine_lifes.util.sendPacket
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.TemptGoal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level

class WanderingArmorStand(entityType: EntityType<out PathfinderMob>, level: Level) : PathfinderMob(entityType, level) {
    init { this.health = 1f }

    private fun canKill(damageSource: DamageSource) = damageSource.`is`(DamageTypes.GENERIC_KILL)

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(WANDER_TICKS, 0)
        builder.define(TICKS_AFTER_KICK, WSTAND_KICK_TICKS)
        builder.define(KICK_TIMES, 0)
    }
    override fun registerGoals() {
        super.registerGoals()
        this.goalSelector.addGoal(1, TemptGoal(this, 0.4, { stack -> stack.`is`(Items.AMETHYST_SHARD) }, false))
        this.goalSelector.addGoal(2, WanderingArmorStandWaterAvoidingRandomStrollGoal(this))
        this.goalSelector.addGoal(3, WanderingArmorStandLookAtPlayerGoal(this))
        this.goalSelector.addGoal(4, WanderingArmorStandRandomLookAroundGoal(this))
    }
    override fun canUsePortal(allowVehicles: Boolean): Boolean = false
    override fun canBeHitByProjectile(): Boolean = false
    override fun canGlide(): Boolean = false
    override fun canBreatheUnderwater(): Boolean = true
    override fun isPushable(): Boolean = false
    override fun push(entity: Entity) {}
    override fun doPush(entity: Entity) {}
    override fun kill(serverLevel: ServerLevel) { if (!serverLevel.isClientSide) remove(RemovalReason.KILLED) }
    private fun kill() = (level() as? ServerLevel)?.let { kill(it) }
    override fun hurtServer(serverLevel: ServerLevel, damageSource: DamageSource, f: Float): Boolean {
        level().playSound(null, x, y, z, NineLifesSounds.GLITCH, SoundSource.AMBIENT)
        val player = damageSource.directEntity as? ServerPlayer
        this.kickTimes++
        this.ticksAfterKick = 0
        if (this.kickTimes == WSTAND_KICK_TIMES || (player?.isCreative ?: false) || canKill(damageSource)) {
            kill()
            return true
        }
        player?.sendPacket(ArmorStandHitEvent(this.position()))
        return false
    }
    override fun isInvulnerableTo(serverLevel: ServerLevel, damageSource: DamageSource): Boolean = !canKill(damageSource)
    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        if (player.getItemInHand(hand).`is`(Items.AMETHYST_SHARD)) {
            player.consumeOneItemInHand(hand)
            repeat(3) {
                level().addParticle(ParticleTypes.HEART,
                    getRandomX(1.0), randomY + 0.5, getRandomZ(1.0),
                    random.nextGaussian() * 0.02, random.nextGaussian() * 0.02, random.nextGaussian() * 0.02
                )
            }
            this.wanderTicks = WSTAND_WANDER_TICKS
            return InteractionResult.SUCCESS
        }
        return super.mobInteract(player, hand)
    }
    override fun tick() {
        super.tick()
        if (this.wanderTicks > 0) this.wanderTicks--
        if (this.ticksAfterKick < WSTAND_KICK_TICKS) this.ticksAfterKick++
        else if (this.ticksAfterKick == WSTAND_KICK_TICKS) this.kickTimes = 0
    }

    val canWander: Boolean
        get() = wanderTicks > 0
    var wanderTicks: Int
        get() = this.entityData.get(WANDER_TICKS)
        set(v) = this.entityData.set(WANDER_TICKS, v.coerceIn(0, WSTAND_WANDER_TICKS))

    var ticksAfterKick: Int
        get() = this.entityData.get(TICKS_AFTER_KICK)
        set(v) = this.entityData.set(TICKS_AFTER_KICK, v.coerceIn(0, WSTAND_KICK_TICKS))

    var kickTimes: Int
        get() = this.entityData.get(KICK_TIMES)
        set(v) = this.entityData.set(KICK_TIMES, v.coerceIn(0, WSTAND_KICK_TIMES))

    companion object {
        private val WANDER_TICKS: EntityDataAccessor<Int> = SynchedEntityData
            .defineId(WanderingArmorStand::class.java, EntityDataSerializers.INT)
        private val TICKS_AFTER_KICK: EntityDataAccessor<Int> = SynchedEntityData
            .defineId(WanderingArmorStand::class.java, EntityDataSerializers.INT)
        private val KICK_TIMES: EntityDataAccessor<Int> = SynchedEntityData
            .defineId(WanderingArmorStand::class.java, EntityDataSerializers.INT)

        fun createAttributes(): AttributeSupplier.Builder = createLivingAttributes()
            .add(Attributes.MAX_HEALTH, 1.0)
            .add(Attributes.FOLLOW_RANGE)
            .add(Attributes.TEMPT_RANGE)
    }
}