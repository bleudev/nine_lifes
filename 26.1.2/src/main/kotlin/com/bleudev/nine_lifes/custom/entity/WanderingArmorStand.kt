package com.bleudev.nine_lifes.custom.entity

import com.bleudev.nine_lifes.custom.entity.ai.goal.WanderingArmorStandLookAtPlayerGoal
import com.bleudev.nine_lifes.custom.entity.ai.goal.WanderingArmorStandRandomLookAroundGoal
import com.bleudev.nine_lifes.custom.entity.ai.goal.WanderingArmorStandWaterAvoidingRandomStrollGoal
import com.bleudev.nine_lifes.custom.packet.payload.ArmorStandHitEvent
import com.bleudev.nine_lifes.util.consumeOneItemInHand
import com.bleudev.nine_lifes.util.sendPacket
import net.minecraft.SharedConstants.TICKS_PER_MINUTE
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
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

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        builder.define(WANDER_TICKS, 0)
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
    override fun hurtServer(serverLevel: ServerLevel, damageSource: DamageSource, f: Float): Boolean {
        (damageSource.directEntity as? ServerPlayer)?.sendPacket(ArmorStandHitEvent(this.position()))
        return damageSource.`is`(DamageTypes.GENERIC_KILL)
    }
    override fun isInvulnerableTo(serverLevel: ServerLevel, damageSource: DamageSource): Boolean =
        !damageSource.`is`(DamageTypes.GENERIC_KILL)

    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        val stack = player.mainHandItem
        if (stack.`is`(Items.AMETHYST_SHARD)) {
            player.consumeOneItemInHand(hand)
            repeat(3) {
                level().addParticle(ParticleTypes.HEART,
                    getRandomX(1.0), randomY + 0.5, getRandomZ(1.0),
                    random.nextGaussian() * 0.02, random.nextGaussian() * 0.02, random.nextGaussian() * 0.02
                )
            }
            this.wanderTicks = 5 * TICKS_PER_MINUTE
            return InteractionResult.SUCCESS
        }
        return super.mobInteract(player, hand)
    }
    override fun tick() {
        super.tick()
        if (this.wanderTicks > 0) this.wanderTicks--
    }

    val canWander: Boolean
        get() = wanderTicks > 0
    var wanderTicks: Int
        get() = this.entityData.get(WANDER_TICKS)
        set(v) = this.entityData.set(WANDER_TICKS, v)

    companion object {
        private val WANDER_TICKS: EntityDataAccessor<Int> = SynchedEntityData
            .defineId(WanderingArmorStand::class.java, EntityDataSerializers.INT)

        fun createAttributes(): AttributeSupplier.Builder = createLivingAttributes()
            .add(Attributes.MAX_HEALTH, 1.0)
            .add(Attributes.FOLLOW_RANGE)
            .add(Attributes.TEMPT_RANGE)
    }
}