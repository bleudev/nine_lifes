package com.bleudev.nine_lifes.util

import com.bleudev.nine_lifes.MAX_LIFES
import com.bleudev.nine_lifes.custom.NineLifesMobEffects
import com.bleudev.nine_lifes.custom.packet.payload.UpdateLifesCount
import com.bleudev.nine_lifes.interfaces.mixin.CustomServerPlayer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.level.GameType

private fun clamp(lifes: Int): Int = lifes
    .coerceAtLeast(0)
    .coerceAtMost(MAX_LIFES)

fun getLifes(player: ServerPlayer): Int {
    val lifes = (player as CustomServerPlayer).`nl$getLifes`()
    if (clamp(lifes) != lifes) setLifes(player, clamp(lifes))
    return clamp(lifes)
}

fun setLifes(player: ServerPlayer, newLifes: Int) {
    val newLifes = clamp(newLifes)
    (player as CustomServerPlayer).`nl$setLifes`(newLifes)
    player.sendPacket(UpdateLifesCount(newLifes))
}
fun setLifes(player: ServerPlayer, changer: (Int) -> (Int)) = setLifes(player, changer(getLifes(player)))

fun resetLifes(player: ServerPlayer) = setLifes(player, MAX_LIFES)

fun revive(player: ServerPlayer) {
    resetLifes(player)
    if (player.isSpectator) player.setGameMode(GameType.SURVIVAL)
    player.addEffect(MobEffectInstance(NineLifesMobEffects.AMETHYSM, 100, 0))
}