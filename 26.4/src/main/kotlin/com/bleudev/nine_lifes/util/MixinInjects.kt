package com.bleudev.nine_lifes.util

import com.bleudev.nine_lifes.LOGGER
import com.bleudev.nine_lifes.MAX_LIFES
import com.bleudev.nine_lifes.custom.NLCriterions
import com.bleudev.nine_lifes.custom.packet.payload.UpdateLifesCount
import com.bleudev.nine_lifes.interfaces.mixin.ILivingEntity
import com.bleudev.nine_lifes.interfaces.mixin.IServerPlayer
import net.minecraft.core.NonNullList
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

// ServerPlayer
private fun lifesClamp(lifes: Int): Int = lifes.coerceIn(0, MAX_LIFES)
/**
 * Player lifes (in range [0; 9])
 * */
var ServerPlayer.lifes: Int
    get() {
        val lifes = (this as IServerPlayer).`nl$getLifes`()
        val clamped = lifesClamp(lifes)
        if (clamped != lifes) this.lifes = clamped
        return clamped
    }
    set(newLifesCount) {
        val oldLifes = (this as IServerPlayer).`nl$getLifes`()
        val newLifes = lifesClamp(newLifesCount)
        try {
            (this as IServerPlayer).`nl$setLifes`(newLifes)
            sendPacket(UpdateLifesCount(newLifes))
            NLCriterions.LIFES_CHANGE.trigger(this, newLifes - oldLifes, false)
        } catch (e: IllegalArgumentException) {
            LOGGER.error("Set {} lifes operation was failed:\n{}", newLifes, e.stackTrace)
        }
    }
@Throws(IllegalArgumentException::class)
fun ServerPlayer.lifesPlayTime(lifesCount: Int): Int {
    try {
        return (this as IServerPlayer).`nl$getLifesPlayTime`(lifesCount).coerceAtLeast(0)
    } catch (e: NullPointerException) {
        throw IllegalArgumentException(e.message, e)
    }
}
var ServerPlayer.stickUsedTicks: Int
    get() = (this as IServerPlayer).`nl$getStickUsedTicks`().coerceAtLeast(0)
    set(newStickUsedTicks) = (this as IServerPlayer).`nl$setStickUsedTicks`(newStickUsedTicks.coerceAtLeast(0))
// Brewing Stand
@Suppress("UNCHECKED_CAST")
private inline fun <reified T : Any, R : Any> T.getPrivateProperty(name: String): R? {
    val property = T::class.declaredMemberProperties.firstOrNull { it.name == name }
    property?.isAccessible = true
    return property?.get(this) as? R
}
val BrewingStandBlockEntity.items: NonNullList<ItemStack>
    get() = this.getPrivateProperty<BrewingStandBlockEntity, NonNullList<ItemStack>>("items") ?:
            NonNullList.withSize(5, ItemStack.EMPTY)
// Living Entity
var LivingEntity.damageTicks: Int
    get() = (this as ILivingEntity).`nl$getDamageTicks`()
    set(value) = (this as ILivingEntity).`nl$setDamageTicks`(value)
