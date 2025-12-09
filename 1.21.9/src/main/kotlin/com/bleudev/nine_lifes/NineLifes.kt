package com.bleudev.nine_lifes

import com.bleudev.nine_lifes.custom.*
import com.bleudev.nine_lifes.custom.NineLifesEntities.WANDERING_ARMOR_STAND_TYPE
import com.bleudev.nine_lifes.custom.event.EntitySpawnEvents
import com.bleudev.nine_lifes.interfaces.mixin.LivingEntityCustomInterface
import com.bleudev.nine_lifes.networking.Packets
import com.bleudev.nine_lifes.networking.payloads.JoinMessage
import com.bleudev.nine_lifes.networking.payloads.StartChargeScreenEffect
import com.bleudev.nine_lifes.networking.payloads.UpdateCenterHeart
import com.bleudev.nine_lifes.util.*
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.projectile.windcharge.WindCharge
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity
import net.minecraft.world.level.entity.EntityTypeTest
import java.util.*

class NineLifes : ModInitializer {
    override fun onInitialize() {
        Packets.initialize()
        CustomEffects.initialize()
        CustomConsumeEffectTypes.initialize()
        NineLifesEnchantments.initialize()
        CustomPotions.initialize()
        CustomEntities.initialize()
        FabricBrewingRecipeRegistryBuilder.BUILD.register(FabricBrewingRecipeRegistryBuilder.BuildCallback { builder: PotionBrewing.Builder? ->
            builder!!.registerPotionRecipe(
                Potions.WATER,
                Ingredient.of(Items.AMETHYST_SHARD),
                CustomPotions.AMETHYSM
            )
        })
        ServerPlayerEvents.JOIN.register { player ->
            if ((!player!!.isSpectator) && (LivesUtils.getLives(player) == 0)) LivesUtils.resetLives(player)
            ServerPlayNetworking.send(player, UpdateCenterHeart(LivesUtils.getLives(player)))
            ServerPlayNetworking.send(player, JoinMessage(LivesUtils.getLives(player)))
            BetaModeHelper.trySendBetaModeMessage(player)
        }
        ServerTickEvents.END_SERVER_TICK.register(ServerTickEvents.EndTick { server: MinecraftServer? ->
            // Custom food (amethyst shard)
            for (player in server!!.playerList.players) ComponentUtils.player_ensure_custom_foods(player)

            for (world in server.allLevels) {
                for (player in world.players()) {
                    val box = ofDXYZ(player.position(), 20)
                    world.getEntities(EntityType.ITEM) { entity ->
                        entityIn<ItemEntity>(box)(entity) &&
                        entity.item.`is`(Items.AMETHYST_SHARD) &&
                        ComponentUtils.should_update_amethyst_shard(entity.item)
                    }.forEach { entity -> entity.item = ComponentUtils.item_ensure_custom_foods(entity.item) }
                    world.getEntities(EntityTypeTest.forClass(LivingEntity::class.java), entityIn(box)).forEach { entity ->
                        val inter = entity as LivingEntityCustomInterface
                        var damage_ticks = inter.`nine_lifes$getDamageTicks`()
                        if (damage_ticks == -1) return@forEach

                        if (damage_ticks == 0) {
                            world.explode(
                                entity,
                                CustomDamageTypes.of(world, CustomDamageTypes.CHARGED_AMETHYST_DAMAGE_TYPE),
                                ExplosionDamageCalculator(),
                                entity.position(),
                                7f,
                                true,
                                Level.ExplosionInteraction.MOB
                            )
                            entity.hurtServer(
                                world,
                                CustomDamageTypes.of(entity, CustomDamageTypes.CHARGED_AMETHYST_DAMAGE_TYPE),
                                1000f
                            )
                        }
                        inter.`nine_lifes$setDamageTicks`(--damage_ticks)
                    }
                }
                tryChargeItems(world)
            }

            for (world in server.allLevels) world.getEntities(EntityType.WIND_CHARGE, alwaysTrue())
                .forEach { tryWindChargeFeatures(world, it) }
        })
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            CustomCommands.initialize(dispatcher)
        }
        EntitySpawnEvents.ENTITY_SPAWN.register { entity, world ->
            // If it's armor stand
            if (entity.type.equals(EntityType.ARMOR_STAND)) {
                // Then try spawn wandering one
                if (world.getRandom().nextFloat() < NineLifesConst.WANDERING_ARMOR_STAND_SPAWN_CHANCE) {
                    val newEntity = WANDERING_ARMOR_STAND_TYPE.create(world, EntitySpawnReason.SPAWN_ITEM_USE)
                    if (newEntity != null) {
                        newEntity.setPos(entity.position())
                        newEntity.copyPosition(entity)
                        world.addFreshEntity(newEntity)
                        entity.discard()
                    }
                }
            }
        }
    }

    private fun tryChargeItems(world: ServerLevel) {
        val chargeScreenEffectRadiusDiff =
            NineLifesConst.CHARGE_SCREEN_EFFECT_RADIUS_MAX - NineLifesConst.CHARGE_SCREEN_EFFECT_RADIUS_MIN

        val chargeEnchantment = NineLifesEnchantments.Holders.charge(world.registryAccess())
        world.getEntities(EntityType.LIGHTNING_BOLT, alwaysTrue()).forEach { lightning ->
            world.getEntities(
                EntityType.ITEM,
                entityIn<ItemEntity>(lightning.position(), LIGHTNING_CHARGING_RADIUS)
                .and({ entity -> entity.item.`is`(NineLifesItemTags.LIGHTNING_CHARGEABLE) }))
                .forEach { itemEntity ->
                    if (itemEntity.item.enchantments.getLevel(chargeEnchantment) == 0) {
                        itemEntity.item.enchant(chargeEnchantment, 1)
                        world.players().forEach { player ->
                            val distance = (player.position().distanceTo(itemEntity.position()) - CHARGE_SCREEN_EFFECT_RADIUS_MIN)
                                .coerceAtLeast(.0)
                            val strength =
                                CHARGE_SCREEN_MAX_STRENGTH * (chargeScreenEffectRadiusDiff - distance) / chargeScreenEffectRadiusDiff
                            ServerPlayNetworking.send(player,
                                StartChargeScreenEffect(CHARGE_SCREEN_DURATION, strength.toFloat())
                            )
                        }
                    }
                }
        }
    }

    fun tryWindChargeFeatures(world: ServerLevel, wind_charge: WindCharge) {
        val action_box = ofDXYZ(wind_charge.position(), 3)
        WorldUtils.forBlocksInBox(action_box) { pos: BlockPos ->
            val blockEntity = world.getBlockEntity(pos)
            if (blockEntity is BrewingStandBlockEntity) {
                if (blockEntity.items?.subList(0, 3)?.stream()?.noneMatch { potion ->
                    try {
                        for (eff in Objects.requireNonNull<T?>(potion.get(DataComponentTypes.POTION_CONTENTS))
                            .getEffects()) if (eff.getEffectType()
                                .equals(CustomEffects.AMETHYSM)
                        ) return@noneMatch true
                        return@noneMatch false
                    } catch (ignored: NullPointerException) {
                        return@noneMatch false
                    }
                } ?: true) return@forBlocksInBox

                world.removeBlock(pos, false)
                world.explode(
                    null, CustomDamageTypes.of(world, CustomDamageTypes.CHARGED_AMETHYST_DAMAGE_TYPE), null,
                    pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(),
                    3f, true, Level.ExplosionInteraction.BLOCK
                )
            }
        }

        world.getEntities(EntityTypeTest.forClass(LivingEntity::class.java), entityIn(action_box)).forEach { entity ->
            entity.removeEffect(CustomEffects.AMETHYSM)
        }

        world.players().forEach { player ->
            if (!action_box.contains(player.position())) return@forEach
            val inventory = player.getInventory()
            var inventory_updated = false
            for (slot in 0..<inventory.toList().size) {
                val stack = inventory.getItem(slot)
                var new_stack = stack.copy()

                val potion: PotionContents?
                if ((stack.get(DataComponents.POTION_CONTENTS).also { potion = it }) != null)
                    for (effect in potion?.allEffects ?: listOf()) if (effect.effect.equals(CustomEffects.AMETHYSM)) {
                    new_stack = ItemStack.EMPTY
                    break
                }

                if (!ItemStack.isSameItem(stack, new_stack)) {
                    inventory.setItem(slot, new_stack)
                    inventory_updated = true
                }
            }

            // TODO: Broke sound
        }
    }
}