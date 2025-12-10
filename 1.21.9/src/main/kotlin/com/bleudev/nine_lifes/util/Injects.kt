@file:Suppress("UNCHECKED_CAST")

package com.bleudev.nine_lifes.util

import com.bleudev.nine_lifes.interfaces.mixin.CustomLivingEntity
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.core.NonNullList
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

inline fun <reified T : Any, R> T.getPrivateProperty(name: String): R? {
    val property = T::class.declaredMemberProperties.firstOrNull { it.name == name }
    property?.isAccessible = true
    return property?.get(this) as? R
}

val BrewingStandBlockEntity.items: NonNullList<ItemStack>?
    get() = this.getPrivateProperty<BrewingStandBlockEntity, NonNullList<ItemStack>>("items")

var LivingEntity.damageTicks: Int
    get() = (this as CustomLivingEntity).`nl$getDamageTicks`()
    set(value) = (this as CustomLivingEntity).`nl$setDamageTicks`(value)

val ServerPlayer.lifes: Int get() = getLifes(this)
fun ServerPlayer.setLifes(newLifesCount: Int) = setLifes(this, newLifesCount)
fun ServerPlayer.setLifes(lifesCountChanger: (Int) -> Int) = setLifes(this, lifesCountChanger)
fun ServerPlayer.resetLifes() = resetLifes(this)
fun ServerPlayer.revive() = revive(this)

fun ServerPlayer.sendPacket(payload: CustomPacketPayload) = ServerPlayNetworking.send(this, payload)
fun ServerPlayer.sendPackets(vararg payloads: CustomPacketPayload) { for (i in payloads) this.sendPacket(i) }