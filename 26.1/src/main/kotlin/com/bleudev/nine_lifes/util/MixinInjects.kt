package com.bleudev.nine_lifes.util

import com.bleudev.nine_lifes.LOGGER
import com.bleudev.nine_lifes.MAX_LIFES
import com.bleudev.nine_lifes.custom.NineLifesCriterions
import com.bleudev.nine_lifes.custom.packet.payload.UpdateLifesCount
import com.bleudev.nine_lifes.interfaces.mixin.CustomLivingEntity
import com.bleudev.nine_lifes.interfaces.mixin.CustomServerPlayer
import net.minecraft.core.NonNullList
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

// ServerPlayer
private fun lifesClamp(lifes: Int): Int = lifes.coerceIn(0, MAX_LIFES)
val ServerPlayer.lifes: Int get() {
    val lifes = (this as CustomServerPlayer).`nl$getLifes`()
    if (lifesClamp(lifes) != lifes) setLifes(lifesClamp(lifes))
    return lifesClamp(lifes)
}
fun ServerPlayer.setLifes(newLifesCount: Int): Int {
    val oldLifes = (this as CustomServerPlayer).`nl$getLifes`()
    val newLifes = lifesClamp(newLifesCount)
    try {
        (this as CustomServerPlayer).`nl$setLifes`(newLifes)
        sendPacket(UpdateLifesCount(newLifes))
        NineLifesCriterions.LIFES_CHANGE.trigger(this, newLifes - oldLifes, false)
        return newLifes
    } catch (e: IllegalArgumentException) {
        LOGGER.error("Lifes set was failed: {}\n{}", newLifes, e.stackTrace)
        return -1
    }
}
@Throws(IllegalArgumentException::class)
fun ServerPlayer.lifesPlayTime(lifesCount: Int): Int {
    try {
        return(this as CustomServerPlayer).`nl$getLifesPlayTime`(lifesCount).coerceAtLeast(0)
    } catch (e: NullPointerException) {
        throw IllegalArgumentException(e.message, e)
    }
}
// Brewing Stand
@Suppress("UNCHECKED_CAST")
private inline fun <reified T : Any, R> T.getPrivateProperty(name: String): R? {
    val property = T::class.declaredMemberProperties.firstOrNull { it.name == name }
    property?.isAccessible = true
    return property?.get(this) as? R
}
val BrewingStandBlockEntity.items: NonNullList<ItemStack>?
    get() = this.getPrivateProperty<BrewingStandBlockEntity, NonNullList<ItemStack>>("items")
// Living Entity
var LivingEntity.damageTicks: Int
    get() = (this as CustomLivingEntity).`nl$getDamageTicks`()
    set(value) = (this as CustomLivingEntity).`nl$setDamageTicks`(value)
