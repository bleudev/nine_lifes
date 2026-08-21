package com.bleudev.nine_lifes

import com.bleudev.nine_lifes.custom.*
import com.bleudev.nine_lifes.custom.NineLifesEntities.WANDERING_ARMOR_STAND
import com.bleudev.nine_lifes.custom.packet.payload.*
import com.bleudev.nine_lifes.custom.packet.payload.unit.AfterPlayerRespawn
import com.bleudev.nine_lifes.custom.packet.payload.unit.BetaModeMessage
import com.bleudev.nine_lifes.custom.packet.payload.unit.StickGiveHeartScreenEffect
import com.bleudev.nine_lifes.util.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.StatFormatter
import net.minecraft.stats.Stats
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.component.DamageResistant
import net.minecraft.world.level.GameType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity
import net.minecraft.world.level.entity.EntityTypeTest
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.math.abs

class NineLifes : ModInitializer {
    companion object {
        @JvmStatic
        internal val notSafeSleepTicks: HashMap<String, Int> = HashMap()
    }

    private val lightningUsed = ArrayList<UUID>()
    private val stickTakenHeartCountPrev: HashMap<String, Int> = HashMap()

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
        NineLifesItems.initialize()
        NineLifesSounds.initialize()
        NineLifesGameRules.initialize()
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
            server.playerList.players.forEach {
                NineLifesCriterions.trigger(it)
                // Amethyst stick max health modify
                val addMaxHealth = -Mth.lerpInt(((it.stickUsedTicks + STICK_USED_EFFECT_HEART_GIVE_DELAY).toFloat() / STICK_USED_EFFECT_TICKS).coerceAtMost(1f), 0, STICK_USED_EFFECT_MAX_HEALTH_TAKE)
                val prev = stickTakenHeartCountPrev[it.gameProfile.name]
                val shouldUpdate = prev != null && abs(addMaxHealth-prev) >= 2 && (-addMaxHealth) % 2 == 1
                if (prev == null || shouldUpdate) stickTakenHeartCountPrev[it.gameProfile.name] = addMaxHealth
                it.getAttribute(Attributes.MAX_HEALTH)?.let { attr ->
                    val mdf = AttributeModifier(createIdentifier("stick_max_health_modify"), addMaxHealth.toDouble(), AttributeModifier.Operation.ADD_VALUE)
                    attr.removeModifier(mdf)
                    if (mdf.amount < 0) attr.addPermanentModifier(mdf)
                }
                if (shouldUpdate) it.sendPacket(StickGiveHeartScreenEffect.INSTANCE)
                it.sendPacket(UpdateStickUsedTicks(it.stickUsedTicks))
                // Custom foods
                playerEnsureCustomFoods(it)

                val lv = it.level()
                var bl = false
                if (!lv.gameRules.get(NineLifesGameRules.TAKE_LIFES)) bl = true
                if (lv.dimension() == Level.OVERWORLD && !lv.gameRules.get(NineLifesGameRules.TAKE_LIFES_IN_OVERWORLD)) bl = true
                if (lv.dimension() == Level.NETHER && !lv.gameRules.get(NineLifesGameRules.TAKE_LIFES_IN_NETHER)) bl = true
                if (lv.dimension() == Level.END && !lv.gameRules.get(NineLifesGameRules.TAKE_LIFES_IN_END)) bl = true
                it.sendPacket(UpdateForceVanillaDeathScreenState(bl))
            }

            for (entry in notSafeSleepTicks) {
                if (entry.value > 0) entry.setValue(entry.value - 1)
                else notSafeSleepTicks.remove(entry.key)
            }

            for (level in server.allLevels) {
                for (player in level.players()) {
                    val box = ofDXYZ(player.position(), 20)
                    level.getEntities(EntityType.ITEM) { entity ->
                        entityIn<ItemEntity>(box)(entity)
                    }.forEach(::itemEntityEnsureCustomFoods)
                    level.getEntities(EntityTypeTest.forClass(LivingEntity::class.java), entityIn(box)).forEach { entity ->
                        when (entity.damageTicks) {
                            -1 -> return@forEach
                            0 -> {
                                level.explode(
                                    entity.position(),
                                    7f,
                                    { NineLifesDamageTypes.chargedAmethyst(it, entity) },
                                    Level.ExplosionInteraction.MOB,
                                    entity
                                )
                                entity.killCharged()
                            }
                        }
                        entity.damageTicks -= 1
                    }
                }
                tryChargeItems(level)
            }

            for (world in server.allLevels) world.getEntities(EntityType.WIND_CHARGE, alwaysTrue())
                .forEach { tryWindChargeFeatures(world, it) }

            val players = server.playerList.players.toList()
            val hasAmethysm = arrayListOf<ServerPlayer>()
            val hasCharged = arrayListOf<ServerPlayer>()

            val chargeEnchantment = NineLifesEnchantments.Holders.charge(server.registryAccess())
            for (player in players) {
                if (player.hasEffect(NineLifesMobEffects.AMETHYSM)) {
                    hasAmethysm.add(player)
                }
                if (player.inventory.any { it.enchantments.getLevel(chargeEnchantment) > 0 }) {
                    hasCharged.add(player)
                }
            }

            for (player in players) {
                var amethysm = hasAmethysm.toList().minOfOrNull { it.distanceTo(player) } ?: 999f
                var charged = hasCharged.toList().minOfOrNull { it.distanceTo(player) } ?: 999f
                if (amethysm == 0f) amethysm = 999f
                if (charged == 0f) charged = 4f
                player.sendPacket(DistanceUpdate(amethysm, charged))
            }
        }
        ServerEntityEvents.ALLOW_LOAD.register { entity, level, reason, isLoadedFromDisk ->
            if (isLoadedFromDisk || reason != EntitySpawnReason.SPAWN_ITEM_USE) return@register true
            if (entity.type == EntityType.ARMOR_STAND) {
                if (level.getRandom().nextFloat() < WSTAND_SPAWN_CHANCE) {
                    val newEntity = WANDERING_ARMOR_STAND.create(level, EntitySpawnReason.SPAWN_ITEM_USE)
                    if (newEntity != null) {
                        newEntity.copyPosition(entity)
                        level.addFreshEntity(newEntity)
                        return@register false
                    }
                }
            }
            return@register true
        }
        ServerLivingEntityEvents.AFTER_DEATH.register { entity, damageSource ->
            if (entity is ServerPlayer && entity.gameMode().isSurvival) {
                if (
                    damageSource.`is`(NineLifesDamageTypeTags.GIVES_LIFE) &&
                    (damageSource.entity == null || damageSource.entity == entity)
                ) {
                    NineLifesCriterions.LIFES_CHANGE.trigger(entity, 1, true)
                    entity.lifes += 1
                    entity.awardStat(NineLifesStats.USED_CHARGED)
                    NineLifesCriterions.USED_CHARGED_TOTAL.trigger(entity)
                }
                else {
                    val lv = entity.level()
                    var bl = true
                    if (!lv.gameRules.get(NineLifesGameRules.TAKE_LIFES)) bl = false
                    if (lv.dimension() == Level.OVERWORLD && !lv.gameRules.get(NineLifesGameRules.TAKE_LIFES_IN_OVERWORLD)) bl = false
                    if (lv.dimension() == Level.NETHER && !lv.gameRules.get(NineLifesGameRules.TAKE_LIFES_IN_NETHER)) bl = false
                    if (lv.dimension() == Level.END && !lv.gameRules.get(NineLifesGameRules.TAKE_LIFES_IN_END)) bl = false
                    if (bl) entity.lifes -= 1
                }
                if (entity.lifes <= 0) entity.setGameMode(GameType.SPECTATOR)
        } }
        EntitySleepEvents.ALLOW_SLEEPING.register { player, _ ->
            if (player !is ServerPlayer) return@register null
            var problem: Player.BedSleepingProblem? = null
            if (player.hasEffect(NineLifesMobEffects.INSOMNIA))
                problem = PROBLEM_INSOMNIA
            if (!player.hasEffect(NineLifesMobEffects.AMETHYSM))
                when (player.lifes) {
                    in 0..3 -> problem = PROBLEM_NOT_NOW
                    in 4..5 -> {
                        notSafeSleepTicks[player.gameProfile.name] = NOT_SAFE_ANAGLYPH_EVENT_DURATION
                        player.sendPacket(BedSleepingProblemEvent(PacketBedSleepingProblem.NOT_SAFE))
                        problem = Player.BedSleepingProblem.NOT_SAFE
                    }
                }
            if (problem?.message != null) {
                NineLifesCriterions.BED_SLEEPING_PROBLEM.trigger(player, problem)
            }
            problem
        }
        EntitySleepEvents.START_SLEEPING.register { entity, _ ->
            if (entity is ServerPlayer)
                NineLifesCriterions.SUCCESS_SLEEP_WITH_AMETHYSM.trigger(entity)
        }
    }

    private fun tryChargeItems(level: ServerLevel) {
        val chargeScreenEffectRadiusDiff = CHARGE_SCREEN_EFFECT_RADIUS_MAX - CHARGE_SCREEN_EFFECT_RADIUS_MIN
        var maxCount = level.gameRules.get(NineLifesGameRules.MAX_CHARGED_ITEMS_AT_A_TIME)
        if (maxCount == 0) return
        if (maxCount == -1) maxCount = Int.MAX_VALUE

        val chargeEnchantment = NineLifesEnchantments.Holders.charge(level.registryAccess())
        level.getEntities(EntityType.LIGHTNING_BOLT, alwaysTrue()).forEach { lightning ->
            if (lightning.uuid in lightningUsed) return@forEach
            lightningUsed.add(lightning.uuid)

            val itemEntities = level.getEntities(
                EntityType.ITEM,
                entityIn<ItemEntity>(lightning.position(), LIGHTNING_CHARGING_RADIUS)
                .and({ entity -> entity.item.`is`(NineLifesItemTags.LIGHTNING_CHARGEABLE) })
            )
            val original = HashMap<Item, Int>()
            val charged = HashMap<Item, Int>()
            for (item in itemEntities) {
                (if (item.item.enchantments.getLevel(chargeEnchantment) > 0) charged else original)
                .compute(item.item.item) { _, value -> // item.item.item... Understandable
                    (value ?: 0) + item.item.count
                }
                item.remove(Entity.RemovalReason.DISCARDED)
            }
            var remain = maxCount
            while (remain > 0) {
                val kk = original.keys.toList()
                if (kk.isNotEmpty()) {
                    var vv = original[kk[0]]!!
                    var nvv = 0
                    if (vv <= remain) {
                        nvv += vv
                        remain -= vv
                        vv = 0
                    }
                    else {
                        nvv += remain
                        vv -= remain
                        remain = 0
                    }
                    original[kk[0]] = vv
                    if (vv == 0) {
                        original.remove(kk[0])
                    }
                    charged.compute(kk[0]) { _, v -> (v ?: 0) + nvv }
                }
            }
            println("O $original C $charged")

            for (o in original) {
                var vv = o.value
                while (vv > 0) {
                    val cnt = vv.coerceAtMost(o.key.defaultMaxStackSize)
                    val new = EntityType.ITEM.create(level, EntitySpawnReason.CONVERSION) ?: break
                    new.copyPosition(lightning)
                    new.item = ItemStack(o.key, cnt)
                    new.item.set(DataComponents.DAMAGE_RESISTANT, DamageResistant(level.registryAccess().get(NineLifesDamageTypeTags.IS_LIGHTNING_OR_FIRE).get()))
                    level.addFreshEntity(new)
                    vv -= cnt
                }
            }
            for (c in charged) {
                var vv = c.value
                while (vv > 0) {
                    val cnt = vv.coerceAtMost(c.key.defaultMaxStackSize)
                    val new = EntityType.ITEM.create(level, EntitySpawnReason.CONVERSION) ?: break
                    new.copyPosition(lightning)
                    new.item = ItemStack(c.key, cnt)
                    new.item.enchant(chargeEnchantment, 1)
                    new.item.set(DataComponents.DAMAGE_RESISTANT, DamageResistant(level.registryAccess().get(NineLifesDamageTypeTags.IS_LIGHTNING_OR_FIRE).get()))
                    level.addFreshEntity(new)
                    vv -= cnt
                }
            }

            if (charged.count { it.value > 0 } > 0) {
                level.players().forEach { player ->
                    val distance = (player.position().distanceTo(lightning.position()) - CHARGE_SCREEN_EFFECT_RADIUS_MIN)
                        .coerceAtLeast(.0)
                    for ((key, value) in charged) {
                        if (value > 0) {
                            NineLifesCriterions.CHARGE_ITEM.trigger(player, key, distance)
                        }
                    }
                    val strength = CHARGE_SCREEN_MAX_STRENGTH * (chargeScreenEffectRadiusDiff - distance) / chargeScreenEffectRadiusDiff
                    player.sendPacket(StartChargeScreen(CHARGE_SCREEN_DURATION, strength.toFloat()))
                }
            }
        }
    }

    fun tryWindChargeFeatures(level: ServerLevel, windCharge: WindCharge) {
        val actionBox = ofDXYZ(windCharge.position(), 3)
        forBlocksInBox(actionBox) { pos ->
            val blockEntity = level.getBlockEntity(pos)
            if (blockEntity is BrewingStandBlockEntity) {
                if (blockEntity.items.subList(0, 3).stream().noneMatch { potion ->
                    try {
                        for (eff in potion.get(DataComponents.POTION_CONTENTS)?.allEffects ?: listOf())
                            if (eff.effect == NineLifesMobEffects.AMETHYSM) return@noneMatch true
                        return@noneMatch false
                    } catch (_: NullPointerException) {
                        return@noneMatch false
                    }
                }) return@forBlocksInBox 0

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
            for (slot in inventory.toList().indices) {
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
val PROBLEM_INSOMNIA: Player.BedSleepingProblem = Player.BedSleepingProblem(Component.translatable("block.minecraft.bed.insomnia_effect"))

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
