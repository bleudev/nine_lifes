package com.bleudev.nine_lifes.custom.world.item

import com.bleudev.nine_lifes.STICK_USED_EFFECT_MAX_HEALTH_TAKE
import com.bleudev.nine_lifes.STICK_USED_EFFECT_TICKS
import com.bleudev.nine_lifes.util.hurtCharged
import com.bleudev.nine_lifes.util.stickUsedTicks
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import kotlin.math.max

class AmethystStickItem(properties: Properties) : Item(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        var bl = true
        // TODO: Shader effects after use
        // TODO: Heart consume
        context.player?.let { player ->
            if (player.foodData.foodLevel < 10f || (player as? ServerPlayer)?.stickUsedTicks?.let { it > 0 } == true) bl = false
            else {
                player.awardStat(Stats.ITEM_USED.get(this))
                context.itemInHand.hurtAndBreak(1, player, context.hand.asEquipmentSlot())
                player.causeFoodExhaustion(33f)
                player.hurtCharged(max(2f, player.health - player.maxHealth + STICK_USED_EFFECT_MAX_HEALTH_TAKE))
                (player as? ServerPlayer)?.stickUsedTicks = STICK_USED_EFFECT_TICKS
            }
        }

        if (bl) {
            (context.level as? ServerLevel)?.let { level ->
                EntityType.LIGHTNING_BOLT.create(level, {},
                    context.clickedPos, EntitySpawnReason.SPAWN_ITEM_USE, true, false)?.let {
                    level.addFreshEntity(it)
                }
            }
            return InteractionResult.SUCCESS
        } else return InteractionResult.PASS
    }
}