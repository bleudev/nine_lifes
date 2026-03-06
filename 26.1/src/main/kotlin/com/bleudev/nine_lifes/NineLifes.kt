package com.bleudev.nine_lifes

import com.bleudev.nine_lifes.api.event.EntityLifecycleEvents
import com.bleudev.nine_lifes.custom.*
import com.bleudev.nine_lifes.custom.NineLifesEntities.WANDERING_ARMOR_STAND
import com.bleudev.nine_lifes.custom.packet.payload.*
import com.bleudev.nine_lifes.custom.packet.payload.unit.AfterPlayerRespawn
import com.bleudev.nine_lifes.custom.packet.payload.unit.BetaModeMessage
import com.bleudev.nine_lifes.util.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.StatFormatter
import net.minecraft.stats.Stats
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.GameType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity
import net.minecraft.world.level.entity.EntityTypeTest
import net.minecraft.world.phys.Vec3

class NineLifes : ModInitializer {
    companion object {
        @JvmStatic
        val notSafeSleepTicks: HashMap<String, Int> = HashMap()
    }

    override fun onInitialize() {
        NineLifesPackets.initialize()
        NineLifesMobEffects.initialize()
        NineLifesConsumeEffects.initialize()
        NineLifesEnchantments.initialize()
        NineLifesPotions.initialize()
        NineLifesEntities.initialize()
        NineLifesCommands.initialize()
        NineLifesCriterions.initialize()
        NineLifesStats.initialize()
        FabricPotionBrewingBuilder.BUILD.register { it.registerPotionRecipe(
            Potions.WATER,
            Ingredient.of(Items.AMETHYST_SHARD),
            NineLifesPotions.AMETHYSM
        ) }
        ServerPlayerEvents.JOIN.register { player ->
            if ((!player.isSpectator) && player.lifes == 0) player.resetLifes()
            val lifes = player.lifes
            player.sendPackets(UpdateLifesCount(lifes), JoinMessage(lifes))
            if (isInBetaMode()) player.sendPacket(BetaModeMessage.INSTANCE)
        }
        ServerPlayerEvents.AFTER_RESPAWN.register { _, newPlayer, _ ->
            newPlayer.sendPacket(AfterPlayerRespawn.INSTANCE)
        }
        ServerTickEvents.END_SERVER_TICK.register { server ->
            server.playerList.players.forEach(NineLifesCriterions::trigger)

            for (entry in notSafeSleepTicks) {
                if (entry.value > 0) entry.setValue(entry.value - 1)
                else notSafeSleepTicks.remove(entry.key)
            }

            for (player in server.playerList.players) playerEnsureCustomFoods(player)

            for (level in server.allLevels) {
                for (player in level.players()) {
                    val box = ofDXYZ(player.position(), 20)
                    level.getEntities(EntityType.ITEM) { entity ->
                        entityIn<ItemEntity>(box)(entity) &&
                        entity.item.`is`(Items.AMETHYST_SHARD) &&
                        shouldUpdateAmethystShard(entity.item)
                    }.forEach { entity -> entity.item = itemEnsureCustomFoods(entity.item) }
                    level.getEntities(EntityTypeTest.forClass(LivingEntity::class.java), entityIn(box)).forEach { entity ->
                        when (entity.damageTicks) {
                            -1 -> return@forEach
                            0 -> {
                                level.explode(entity.position(), 7f, NineLifesDamageTypes::chargedAmethyst, Level.ExplosionInteraction.MOB, entity)
                                entity.kill(NineLifesDamageTypes::chargedAmethyst)
                            }
                        }
                        entity.damageTicks -= 1
                    }
                }
                tryChargeItems(level)
            }

            for (world in server.allLevels) world.getEntities(EntityType.WIND_CHARGE, alwaysTrue())
                .forEach { tryWindChargeFeatures(world, it) }
        }
        EntityLifecycleEvents.ENTITY_SPAWN.register { entity, level ->
            if (entity.type == EntityType.ARMOR_STAND) {
                if (level.getRandom().nextFloat() < WANDERING_ARMOR_STAND_SPAWN_CHANCE) {
                    val newEntity = WANDERING_ARMOR_STAND.create(level, EntitySpawnReason.SPAWN_ITEM_USE)
                    if (newEntity != null) {
                        newEntity.setPos(entity.position())
                        newEntity.copyPosition(entity)
                        level.addFreshEntity(newEntity)
                        entity.discard()
                    }
                }
            }
        }
        ServerLivingEntityEvents.AFTER_DEATH.register { entity, damageSource ->
            if (entity is ServerPlayer && entity.gameMode().isSurvival) {
                if (damageSource.`is`(NineLifesDamageTypeTags.GIVES_LIFE)) {
                    NineLifesCriterions.LIFES_CHANGE.trigger(entity, 1, true)
                    entity.lifes += 1
                    entity.awardStat(NineLifesStats.USED_CHARGED)
                    NineLifesCriterions.USED_CHARGED_TOTAL.trigger(entity)
                }
                else entity.lifes -= 1
                if (entity.lifes <= 0) entity.setGameMode(GameType.SPECTATOR)
        } }
        EntitySleepEvents.ALLOW_SLEEPING.register { player, _ ->
            if (player !is ServerPlayer) return@register null
            var problem: Player.BedSleepingProblem? = null
            if (!player.hasEffect(NineLifesMobEffects.AMETHYSM))
                when (player.lifes) {
                    in 0..3 -> problem = PROBLEM_NOT_NOW
                    in 4..5 -> {
                        notSafeSleepTicks[player.gameProfile.name] = NOT_SAFE_ANAGLYPH_EVENT_DURATION
                        player.sendPacket(BedSleepingProblemEvent(PacketBedSleepingProblem.NOT_SAFE))
                        problem = Player.BedSleepingProblem.NOT_SAFE
                    }
                }
            if (problem != null && problem.message != null)
                NineLifesCriterions.BED_SLEEPING_PROBLEM.trigger(player, problem)
            problem
        }
        EntitySleepEvents.START_SLEEPING.register { entity, _ ->
            if (entity is ServerPlayer)
                NineLifesCriterions.SUCCESS_SLEEP_WITH_AMETHYSM.trigger(entity)
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
                            NineLifesCriterions.CHARGE_ITEM.trigger(player, itemEntity.item.item, distance)
                            val strength = CHARGE_SCREEN_MAX_STRENGTH * (chargeScreenEffectRadiusDiff - distance) / chargeScreenEffectRadiusDiff
                            player.sendPacket(StartChargeScreen(CHARGE_SCREEN_DURATION, strength.toFloat()))
                        }
                    }
                }
        }
    }

    fun tryWindChargeFeatures(level: ServerLevel, windCharge: WindCharge) {
        val actionBox = ofDXYZ(windCharge.position(), 3)
        forBlocksInBox(actionBox) { pos ->
            val blockEntity = level.getBlockEntity(pos)
            if (blockEntity is BrewingStandBlockEntity) {
                if (blockEntity.items?.subList(0, 3)?.stream()?.noneMatch { potion ->
                    try {
                        for (eff in potion.get(DataComponents.POTION_CONTENTS)?.allEffects ?: listOf())
                            if (eff.effect == NineLifesMobEffects.AMETHYSM) return@noneMatch true
                        return@noneMatch false
                    } catch (_: NullPointerException) {
                        return@noneMatch false
                    }
                } ?: true) return@forBlocksInBox 0

                level.removeBlock(pos, false)
                level.explode(Vec3(pos), 3f, NineLifesDamageTypes::chargedAmethyst, Level.ExplosionInteraction.BLOCK)
            }
            0
        }
        level.getEntities(EntityTypeTest.forClass(LivingEntity::class.java), entityIn(actionBox)).forEach {
            it.removeEffect(NineLifesMobEffects.AMETHYSM)
        }

        level.players().forEach { player ->
            if (!actionBox.contains(player.position())) return@forEach
            val inventory = player.inventory
            var inventoryUpdated = false
            for (slot in 0..<inventory.toList().size) {
                val stack = inventory.getItem(slot)
                var newStack = stack.copy()

                val potion: PotionContents?
                if ((stack.get(DataComponents.POTION_CONTENTS).also { potion = it }) != null)
                    for (effect in potion?.allEffects ?: listOf()) if (effect.effect == NineLifesMobEffects.AMETHYSM) {
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

val PROBLEM_NOT_NOW: Player.BedSleepingProblem = Player.BedSleepingProblem(Component.translatable("block.minecraft.bed.no_sleep"))

object NineLifesStats {
    val USED_CHARGED: Identifier = makeCustomStat("used_charged", StatFormatter.DEFAULT)

    private fun makeCustomStat(id: String, formatter: StatFormatter): Identifier {
        val location = createIdentifier(id)
        Registry.register(BuiltInRegistries.CUSTOM_STAT, id, location)
        Stats.CUSTOM.get(location, formatter)
        return location
    }

    internal fun initialize() {}
}
