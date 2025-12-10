package com.bleudev.nine_lifes

import com.bleudev.nine_lifes.custom.*
import com.bleudev.nine_lifes.custom.NineLifesEntities.WANDERING_ARMOR_STAND_TYPE
import com.bleudev.nine_lifes.custom.event.EntitySpawnEvents
import com.bleudev.nine_lifes.custom.packet.payload.BetaModeMessage
import com.bleudev.nine_lifes.custom.packet.payload.JoinMessage
import com.bleudev.nine_lifes.custom.packet.payload.StartChargeScreen
import com.bleudev.nine_lifes.custom.packet.payload.UpdateLifesCount
import com.bleudev.nine_lifes.interfaces.mixin.LivingEntityCustomInterface
import com.bleudev.nine_lifes.util.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.projectile.windcharge.WindCharge
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity
import net.minecraft.world.level.entity.EntityTypeTest

class NineLifes : ModInitializer {
    override fun onInitialize() {
        NineLifesPackets.initialize()
        NineLifesMobEffects.initialize()
        CustomConsumeEffectTypes.initialize()
        NineLifesEnchantments.initialize()
        CustomPotions.initialize()
        NineLifesEntities.initialize()
        FabricBrewingRecipeRegistryBuilder.BUILD.register { it.registerPotionRecipe(
            Potions.WATER,
            Ingredient.of(Items.AMETHYST_SHARD),
            CustomPotions.AMETHYSM
        ) }
        ServerPlayerEvents.JOIN.register { player ->
            if ((!player!!.isSpectator) && (LivesUtils.getLives(player) == 0)) LivesUtils.resetLives(player)
            val lifes = LivesUtils.getLives(player)
            player.sendPackets(UpdateLifesCount(lifes), JoinMessage(lifes))
            if (isInBetaMode()) player.sendPacket(BetaModeMessage())
        }
        ServerTickEvents.END_SERVER_TICK.register { server ->
            for (player in server.playerList.players) player_ensure_custom_foods(player)

            for (level in server.allLevels) {
                for (player in level.players()) {
                    val box = ofDXYZ(player.position(), 20)
                    level.getEntities(EntityType.ITEM) { entity ->
                        entityIn<ItemEntity>(box)(entity) &&
                        entity.item.`is`(Items.AMETHYST_SHARD) &&
                        should_update_amethyst_shard(entity.item)
                    }.forEach { entity -> entity.item = item_ensure_custom_foods(entity.item) }
                    level.getEntities(EntityTypeTest.forClass(LivingEntity::class.java), entityIn(box)).forEach { entity ->
                        val inter = entity as LivingEntityCustomInterface
                        var damageTicks = inter.`nine_lifes$getDamageTicks`()
                        if (damageTicks == -1) return@forEach

                        if (damageTicks == 0) {
                            level.explode(entity, NineLifesDamageSources.charged(level),
                                ExplosionDamageCalculator(), entity.position(),
                                7f, true, Level.ExplosionInteraction.MOB)
                            entity.hurtServer(level, NineLifesDamageSources.charged(level), 1000f)
                        }
                        inter.`nine_lifes$setDamageTicks`(--damageTicks)
                    }
                }
                tryChargeItems(level)
            }

            for (world in server.allLevels) world.getEntities(EntityType.WIND_CHARGE, alwaysTrue())
                .forEach { tryWindChargeFeatures(world, it) }
        }
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            CustomCommands.initialize(dispatcher)
        }
        EntitySpawnEvents.ENTITY_SPAWN.register { entity, level ->
            // If it's armor stand
            if (entity.type.equals(EntityType.ARMOR_STAND)) {
                // Then try spawn wandering one
                if (level.getRandom().nextFloat() < WANDERING_ARMOR_STAND_SPAWN_CHANCE) {
                    val newEntity = WANDERING_ARMOR_STAND_TYPE.create(level, EntitySpawnReason.SPAWN_ITEM_USE)
                    if (newEntity != null) {
                        newEntity.setPos(entity.position())
                        newEntity.copyPosition(entity)
                        level.addFreshEntity(newEntity)
                        entity.discard()
                    }
                }
            }
        }
    }

    private fun tryChargeItems(level: ServerLevel) {
        val chargeScreenEffectRadiusDiff = CHARGE_SCREEN_EFFECT_RADIUS_MAX - CHARGE_SCREEN_EFFECT_RADIUS_MIN

        val chargeEnchantment = NineLifesEnchantments.Holders.charge(level.registryAccess())
        level.getEntities(EntityType.LIGHTNING_BOLT, alwaysTrue()).forEach { lightning ->
            level.getEntities(
                EntityType.ITEM,
                entityIn<ItemEntity>(lightning.position(), LIGHTNING_CHARGING_RADIUS)
                .and({ entity -> entity.item.`is`(NineLifesItemTags.LIGHTNING_CHARGEABLE) }))
                .forEach { itemEntity ->
                    if (itemEntity.item.enchantments.getLevel(chargeEnchantment) == 0) {
                        itemEntity.item.enchant(chargeEnchantment, 1)
                        level.players().forEach { player ->
                            val distance = (player.position().distanceTo(itemEntity.position()) - CHARGE_SCREEN_EFFECT_RADIUS_MIN)
                                .coerceAtLeast(.0)
                            val strength = CHARGE_SCREEN_MAX_STRENGTH * (chargeScreenEffectRadiusDiff - distance) / chargeScreenEffectRadiusDiff
                            player.sendPacket(StartChargeScreen(CHARGE_SCREEN_DURATION, strength.toFloat()))
                        }
                    }
                }
        }
    }

    fun tryWindChargeFeatures(level: ServerLevel, windCharge: WindCharge) {
        val actionBox = ofDXYZ(windCharge.position(), 3)
        forBlocksInBox(actionBox) { pos: BlockPos ->
            val blockEntity = level.getBlockEntity(pos)
            if (blockEntity is BrewingStandBlockEntity) {
                if (blockEntity.items?.subList(0, 3)?.stream()?.noneMatch { potion ->
                    try {
                        for (eff in potion.get(DataComponents.POTION_CONTENTS)?.allEffects ?: listOf())
                            if (eff.effect.equals(NineLifesMobEffects.AMETHYSM)) return@noneMatch true
                        return@noneMatch false
                    } catch (_: NullPointerException) {
                        return@noneMatch false
                    }
                } ?: true) return@forBlocksInBox 0

                level.removeBlock(pos, false)
                level.explode(
                    null, NineLifesDamageSources.charged(level), null,
                    pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(),
                    3f, true, Level.ExplosionInteraction.BLOCK
                )
            }
            0
        }

        level.getEntities(EntityTypeTest.forClass(LivingEntity::class.java), entityIn(actionBox)).forEach { entity ->
            entity.removeEffect(NineLifesMobEffects.AMETHYSM)
        }

        level.players().forEach { player ->
            if (!actionBox.contains(player.position())) return@forEach
            val inventory = player.getInventory()
            var inventoryUpdated = false
            for (slot in 0..<inventory.toList().size) {
                val stack = inventory.getItem(slot)
                var newStack = stack.copy()

                val potion: PotionContents?
                if ((stack.get(DataComponents.POTION_CONTENTS).also { potion = it }) != null)
                    for (effect in potion?.allEffects ?: listOf()) if (effect.effect.equals(NineLifesMobEffects.AMETHYSM)) {
                    newStack = ItemStack.EMPTY
                    break
                }

                if (!ItemStack.isSameItem(stack, newStack)) {
                    inventory.setItem(slot, newStack)
                    inventoryUpdated = true
                }
            }

            // TODO: Broke sound
        }
    }
}