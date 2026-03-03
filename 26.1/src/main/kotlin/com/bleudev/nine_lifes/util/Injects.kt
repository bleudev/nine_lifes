package com.bleudev.nine_lifes.util

import com.bleudev.nine_lifes.MAX_LIFES
import com.bleudev.nine_lifes.custom.NineLifesMobEffects
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.permissions.Permissions
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.GameType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

// Lang
fun <T> T?.requireNotNullOr(action: () -> Unit): T? {
    if (this == null) action()
    return this
}

fun Float.lerp(start: Float = 0f, end: Float = 1f): Float = start + coerceIn(0f, 1f) * (end - start)

// Lifes
fun ServerPlayer.setLifes(lifesCountChanger: (Int) -> Int) = setLifes(lifesCountChanger(this@setLifes.lifes))
fun ServerPlayer.addLifes(addedLifesCount: Int) = setLifes { it + addedLifesCount }
fun ServerPlayer.resetLifes() = setLifes(MAX_LIFES)
fun ServerPlayer.revive() {
    resetLifes()
    if (isSpectator) setGameMode(GameType.SURVIVAL)
    addEffect(MobEffectInstance(NineLifesMobEffects.AMETHYSM, 100, 0))
}

// Networking
fun ServerPlayer.sendPacket(payload: CustomPacketPayload) = ServerPlayNetworking.send(this, payload)
fun ServerPlayer.sendPackets(vararg payloads: CustomPacketPayload) { for (i in payloads) this.sendPacket(i) }

// Utils
fun Player.consumeOneItemInHand(hand: InteractionHand) {
    if (this.gameMode()?.isSurvival == false) return
    val stack = this.getItemInHand(hand)
    if (stack.count == 1) setItemInHand(hand, ItemStack.EMPTY)
    else {
        stack.count -= 1
        setItemInHand(hand, stack)
    }
}
val ServerPlayer.playTime: Int get() = this.stats.getValue(Stats.CUSTOM, Stats.PLAY_TIME)
val ServerPlayer.currentLifesPlayTime: Int get() = this.lifesPlayTime(this.lifes)
fun Level.explode(pos: Vec3, strength: Float, damageSourceSupplier: (Level) -> DamageSource, explosionInteraction: Level.ExplosionInteraction, source: Entity? = null) = explode(
    source, damageSourceSupplier(this), ExplosionDamageCalculator(),
    pos.x, pos.y, pos.z, strength, true, explosionInteraction
)
fun LivingEntity.kill(damageSourceSupplier: (Level) -> DamageSource) = (level() as? ServerLevel)?.let {
    hurtServer(it, damageSourceSupplier(it), Float.MAX_VALUE)
}

// Commands
fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.requiresAdmin(): T =
    requires { it.permissions().hasPermission(Permissions.COMMANDS_ADMIN) }

fun SuggestionsBuilder.suggestMany(vararg integers: Int) = this.apply { for (i in integers) this.suggest(i) }