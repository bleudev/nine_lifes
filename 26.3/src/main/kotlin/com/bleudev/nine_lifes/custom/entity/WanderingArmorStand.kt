package com.bleudev.nine_lifes.custom.entity

import com.bleudev.nine_lifes.*
import com.bleudev.nine_lifes.custom.NineLifesDamageTypeTags
import com.bleudev.nine_lifes.custom.NineLifesSounds
import com.bleudev.nine_lifes.custom.entity.ai.goal.WanderingArmorStandLookAtPlayerGoal
import com.bleudev.nine_lifes.custom.entity.ai.goal.WanderingArmorStandRandomLookAroundGoal
import com.bleudev.nine_lifes.custom.entity.ai.goal.WanderingArmorStandWaterAvoidingRandomStrollGoal
import com.bleudev.nine_lifes.custom.packet.payload.ArmorStandHitEvent
import com.bleudev.nine_lifes.custom.packet.payload.unit.ArmorStandKillEvent
import com.bleudev.nine_lifes.util.consumeOneItemInHand
import com.bleudev.nine_lifes.util.hurtUnknown
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
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.TemptGoal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class WanderingArmorStand(entityType: EntityType<out PathfinderMob>, level: Level) : PathfinderMob(entityType, level) {
    init { this.health = 1f }

    var lastHit: Long = 0

    private fun sourceCanInstaKill(damageSource: DamageSource) = damageSource.`is`(DamageTypes.GENERIC_KILL)
    private fun sourceCanHit(damageSource: DamageSource) = sourceCanInstaKill(damageSource) ||
        damageSource.`is`(NineLifesDamageTypeTags.CAN_HIT_WSTAND)
    private fun playerCanHit(player: ServerPlayer): Boolean = !player.gameMode().isSurvival || player.foodData.foodLevel >= 5f
    private fun triedKillReact(player: ServerPlayer) {
        if (!player.gameMode().isSurvival) return
        if (player.random.nextFloat() >= 0.5f) return
        player.hurtUnknown(player.random.nextInt(6))
        player.causeFoodExhaustion(20f)
    }

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
    override fun isAffectedByFluids(): Boolean = false
    override fun kill(serverLevel: ServerLevel) { if (!serverLevel.isClientSide) remove(RemovalReason.KILLED) }
    private fun kill() = (level() as? ServerLevel)?.let { kill(it) }
    override fun hurtServer(serverLevel: ServerLevel, damageSource: DamageSource, f: Float): Boolean {
        if (!sourceCanHit(damageSource)) return false
        val player = damageSource.directEntity as? ServerPlayer
        if (player != null && !playerCanHit(player)) return false
        this.kickTimes++
        this.ticksAfterKick = 0
        val bl = this.kickTimes == WSTAND_KICK_TIMES || (player?.isCreative ?: false) || sourceCanInstaKill(damageSource)
        val snd = if (bl) NineLifesSounds.ENTITY_WANDERING_ARMOR_STAND_DEATH else NineLifesSounds.ENTITY_WANDERING_ARMOR_STAND_HURT
        val pitch = if (bl) 0.8f else 0.95f
        val rad = if (bl) WSTAND_KILL_EVENT_RADIUS else WSTAND_KICK_EVENT_RADIUS
        level().playSound(null, x, y, z, snd, SoundSource.AMBIENT, 1f, pitch)

        serverLevel.getPlayers { it.position().distanceToSqr(this.position()) <= rad*rad }.forEach {
            it.sendPacket(ArmorStandHitEvent(this.position()))
            if (bl) it.sendPacket(ArmorStandKillEvent.INSTANCE)
        }
        if (bl) kill()
        else lastHit = serverLevel.gameTime
        player?.let { triedKillReact(it) }

        return bl
    }
    override fun isInvulnerableTo(serverLevel: ServerLevel, damageSource: DamageSource): Boolean = !sourceCanInstaKill(damageSource)
    override fun interact(player: Player, hand: InteractionHand, location: Vec3): InteractionResult {
        val itemStack = player.getItemInHand(hand)
        if (!itemStack.`is`(Items.NAME_TAG)) {
            if (player.isSpectator) {
                return InteractionResult.SUCCESS
            } else if (player.level().isClientSide) {
                return InteractionResult.SUCCESS_SERVER
            } else {
                val itemInHandSlot = this.getEquipmentSlotForItem(itemStack)
                if (itemStack.isEmpty) {
                    val clickedSlot = this.getClickedSlot(location)
                    val targetSlot = if (this.isDisabled(clickedSlot)) itemInHandSlot else clickedSlot
                    if (this.hasItemInSlot(targetSlot) && this.swapItem(player, targetSlot, itemStack, hand)) {
                        return InteractionResult.SUCCESS_SERVER
                    }
                } else {
                    // WStand feeding
                    if (itemStack.`is`(Items.AMETHYST_SHARD) && feed(player, hand)) {
                        return InteractionResult.SUCCESS_SERVER
                    }
                    // Armor stand logic
                    if (this.isDisabled(itemInHandSlot)) {
                        return InteractionResult.FAIL
                    }
                    if (this.swapItem(player, itemInHandSlot, itemStack, hand)) {
                        return InteractionResult.SUCCESS_SERVER
                    }
                }
                return super.interact(player, hand, location)
            }
        } else {
            return super.interact(player, hand, location)
        }
    }

    override fun tick() {
        super.tick()
        if (this.wanderTicks > 0) this.wanderTicks--
        if (this.ticksAfterKick < WSTAND_KICK_TICKS) this.ticksAfterKick++
        else if (this.ticksAfterKick == WSTAND_KICK_TICKS) this.kickTimes = 0
    }

    private fun feed(player: Player, hand: InteractionHand): Boolean {
        player.consumeOneItemInHand(hand)
        repeat(3) {
            level().addParticle(ParticleTypes.HEART,
                getRandomX(1.0), randomY + 0.5, getRandomZ(1.0),
                random.nextGaussian() * 0.02, random.nextGaussian() * 0.02, random.nextGaussian() * 0.02
            )
        }
        this.wanderTicks = WSTAND_WANDER_TICKS
        return true
    }

    /** From `ArmorStand.getClickedSlot` */
    private fun getClickedSlot(location: Vec3): EquipmentSlot {
        var slotClicked: EquipmentSlot = EquipmentSlot.MAINHAND
        val clickYPosition = location.y / (this.scale * this.ageScale).toDouble()
        if (clickYPosition in 0.1..<0.55 && this.hasItemInSlot(EquipmentSlot.FEET)) {
            slotClicked = EquipmentSlot.FEET
        } else if (clickYPosition in 0.9..<1.6 && this.hasItemInSlot(EquipmentSlot.CHEST)) {
            slotClicked = EquipmentSlot.CHEST
        } else if (clickYPosition in 0.4..<1.2 && this.hasItemInSlot(EquipmentSlot.LEGS)) {
            slotClicked = EquipmentSlot.LEGS
        } else if (clickYPosition >= 1.6 && this.hasItemInSlot(EquipmentSlot.HEAD)) {
            slotClicked = EquipmentSlot.HEAD
        } else if (!this.hasItemInSlot(EquipmentSlot.MAINHAND) && this.hasItemInSlot(EquipmentSlot.OFFHAND)) {
            slotClicked = EquipmentSlot.OFFHAND
        }
        return slotClicked
    }

    /** From `ArmorStand.isDisabled`, but modified */
    private fun isDisabled(slot: EquipmentSlot): Boolean {
        return slot.type == EquipmentSlot.Type.HAND
    }

    /** From `ArmorStand.swapItem`, but modified */
    private fun swapItem(player: Player, slot: EquipmentSlot, playerItemStack: ItemStack, hand: InteractionHand): Boolean {
        val itemStack = this.getItemBySlot(slot)
        if (player.hasInfiniteMaterials() && itemStack.isEmpty && !playerItemStack.isEmpty) {
            this.setItemSlot(slot, playerItemStack.copyWithCount(1))
            return true
        } else if (playerItemStack.count > 1) {
            if (!itemStack.isEmpty) {
                return false
            } else {
                this.setItemSlot(slot, playerItemStack.split(1))
                return true
            }
        } else {
            this.setItemSlot(slot, playerItemStack)
            player.setItemInHand(hand, itemStack)
            return true
        }
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