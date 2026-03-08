package com.bleudev.nine_lifes.custom.world.item

import net.minecraft.server.level.ServerLevel
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext

class AmethystStickItem(properties: Properties) : Item(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        var bl = true
        // TODO: Shader effects after use
        // TODO: Heart consume
        context.player?.let { player ->
            if (player.foodData.foodLevel < 10f) bl = false
            else {
                player.awardStat(Stats.ITEM_USED.get(this))
                context.itemInHand.hurtAndBreak(1, player, context.hand.asEquipmentSlot())
                player.causeFoodExhaustion(33f)
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