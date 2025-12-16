@file:Suppress("UNCHECKED_CAST")

package com.bleudev.nine_lifes.util

import com.bleudev.nine_lifes.MAX_LIFES
import com.bleudev.nine_lifes.custom.NineLifesMobEffects
import com.bleudev.nine_lifes.custom.packet.payload.UpdateLifesCount
import com.bleudev.nine_lifes.interfaces.mixin.CustomLivingEntity
import com.bleudev.nine_lifes.interfaces.mixin.CustomServerPlayer
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.commands.CommandSourceStack
import net.minecraft.core.NonNullList
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameType
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

// Lang
inline fun <reified T : Any, R> T.getPrivateProperty(name: String): R? {
    val property = T::class.declaredMemberProperties.firstOrNull { it.name == name }
    property?.isAccessible = true
    return property?.get(this) as? R
}

fun <T> T?.requireNotNullOr(action: () -> Unit): T? {
    if (this == null) action()
    return this
}

fun Float.lerp(start: Float = 0f, end: Float = 1f): Float = start + coerceIn(0f, 1f) * (end - start)

// Mixin
val BrewingStandBlockEntity.items: NonNullList<ItemStack>?
    get() = this.getPrivateProperty<BrewingStandBlockEntity, NonNullList<ItemStack>>("items")

var LivingEntity.damageTicks: Int
    get() = (this as CustomLivingEntity).`nl$getDamageTicks`()
    set(value) = (this as CustomLivingEntity).`nl$setDamageTicks`(value)

// Lifes
private fun lifesClamp(lifes: Int): Int = lifes.coerceIn(0, MAX_LIFES)

val ServerPlayer.lifes: Int get() {
    val lifes = (this as CustomServerPlayer).`nl$getLifes`()
    if (lifesClamp(lifes) != lifes) setLifes(lifesClamp(lifes))
    return lifesClamp(lifes)
}
fun ServerPlayer.setLifes(newLifesCount: Int) {
    val newLifes = lifesClamp(newLifesCount)
    (this as CustomServerPlayer).`nl$setLifes`(newLifes)
    sendPacket(UpdateLifesCount(newLifes))
}
fun ServerPlayer.setLifes(lifesCountChanger: (Int) -> Int) = setLifes(lifesCountChanger(lifes))
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
    val stack = this.getItemInHand(hand)
    if (stack.count == 1) setItemInHand(hand, ItemStack.EMPTY)
    else {
        stack.count -= 1
        setItemInHand(hand, stack)
    }
}

// Commands
fun <T : ArgumentBuilder<CommandSourceStack, T>> ArgumentBuilder<CommandSourceStack, T>.requiresAdmin(): T =
    requires { it.hasPermission(3) }

fun SuggestionsBuilder.suggestMany(vararg integers: Int) = this.apply { for (i in integers) this.suggest(i) }