package com.bleudev.nine_lifes;

import com.bleudev.nine_lifes.compat.VersionCompat;
import com.bleudev.nine_lifes.custom.*;
import com.bleudev.nine_lifes.custom.event.EntitySpawnEvents;
import com.bleudev.nine_lifes.interfaces.mixin.LivingEntityCustomInterface;
import com.bleudev.nine_lifes.networking.Packets;
import com.bleudev.nine_lifes.networking.payloads.JoinMessage;
import com.bleudev.nine_lifes.networking.payloads.StartChargeScreenEffect;
import com.bleudev.nine_lifes.networking.payloads.UpdateCenterHeart;
import com.bleudev.nine_lifes.util.ComponentUtils;
import com.bleudev.nine_lifes.util.LivesUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.explosion.EntityExplosionBehavior;

import static com.bleudev.nine_lifes.NineLifesConst.*;
import static com.bleudev.nine_lifes.compat.VersionCompat.getPosCompat;
import static com.bleudev.nine_lifes.util.ComponentUtils.item_ensure_custom_foods;
import static com.bleudev.nine_lifes.util.ComponentUtils.should_update_amethyst_shard;

public class Nine_lifes implements ModInitializer {
    @Deprecated(since = "1.9.1")
    public static final String MOD_ID = NineLifesConst.MOD_ID;

    @Override
    public void onInitialize() {
        Packets.initialize();
        CustomEffects.initialize();
        CustomConsumeEffectTypes.initialize();
        CustomEnchantments.initialize();
        CustomPotions.initialize();
        CustomEntities.initialize();
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder ->
            builder.registerPotionRecipe(Potions.WATER, Items.AMETHYST_SHARD, CustomPotions.AMETHYSM));
        ServerPlayerEvents.JOIN.register(player -> {
            if ((!player.isSpectator()) && (LivesUtils.getLives(player) == 0))
                LivesUtils.resetLives(player);
            ServerPlayNetworking.send(player, new UpdateCenterHeart(LivesUtils.getLives(player)));
            ServerPlayNetworking.send(player, new JoinMessage(LivesUtils.getLives(player)));
            BetaModeHelper.trySendBetaModeMessage(player);
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            // Custom food (amethyst shard)
            for (var player: server.getPlayerManager().getPlayerList())
                ComponentUtils.player_ensure_custom_foods(player);

            // Find all amethyst shard item entities in `amethyst_shard_find_radius`
            double amethyst_shard_find_radius = 20;
            for (var world: server.getWorlds()) {
                for (var player: world.getPlayers()) {
                    var box = Box.of(getPosCompat(player), amethyst_shard_find_radius, amethyst_shard_find_radius, amethyst_shard_find_radius);
                    world.getEntitiesByType(
                        EntityType.ITEM,
                        box,
                        entity -> entity.getStack().isOf(Items.AMETHYST_SHARD) &&
                                         should_update_amethyst_shard(entity.getStack())
                    ).forEach(entity -> entity.setStack(item_ensure_custom_foods(entity.getStack())));
                    world.getEntitiesByClass(LivingEntity.class, box, ignored -> true).forEach(entity -> {
                        var inter = (LivingEntityCustomInterface) entity;
                        int damage_ticks = inter.nine_lifes$getDamageTicks();
                        if (damage_ticks == -1) return;

                        if (damage_ticks == 0) {
                            world.createExplosion(
                                    entity, CustomDamageTypes.of(world, CustomDamageTypes.CHARGED_AMETHYST_DAMAGE_TYPE), new EntityExplosionBehavior(entity),
                                    getPosCompat(entity),
                                    7f, true, World.ExplosionSourceType.MOB
                            );
                            entity.damage(world,
                                CustomDamageTypes.of(entity, CustomDamageTypes.CHARGED_AMETHYST_DAMAGE_TYPE),
                                1000f
                            );
                        }
                        inter.nine_lifes$setDamageTicks(--damage_ticks);
                    });
                }
                tryChargeItems(world);
            }

            // Wind charges
            for (var world: server.getWorlds())
                world.getEntitiesByType(EntityType.WIND_CHARGE, ignored -> true).forEach(wind_charge ->
                    WindChargeTickFeatures.do_for(world, wind_charge));
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CustomCommands.initialize(dispatcher);
        });
        EntitySpawnEvents.ENTITY_SPAWN.register((entity, world) -> {
            // If it's armor stand
            if (entity.getType().equals(EntityType.ARMOR_STAND)) {
                // Then try spawn wandering one
                if (world.getRandom().nextFloat() < WANDERING_ARMOR_STAND_SPAWN_CHANCE) {
                    var newEntity = CustomEntities.WANDERING_ARMOR_STAND_TYPE.create(world, SpawnReason.SPAWN_ITEM_USE);
                    if (newEntity != null) {
                        newEntity.refreshPositionAndAngles(VersionCompat.getPosCompat(entity), entity.getYaw(), entity.getPitch());
                        world.spawnEntity(newEntity);
                        entity.discard();
                    }
                }
            }
        });
    }

    private void tryChargeItems(ServerWorld world) {
        final int charge_screen_effect_radius_diff = CHARGE_SCREEN_EFFECT_RADIUS_MAX - CHARGE_SCREEN_EFFECT_RADIUS_MIN;

        final var CHARGE_ENCHANTMENT = CustomEnchantments.Entries.create(world.getRegistryManager()).charge();
        world.getEntitiesByType(EntityType.LIGHTNING_BOLT, ignored -> true).forEach(lightning ->
            world.getEntitiesByType(EntityType.ITEM,
                Box.of(getPosCompat(lightning), LIGHTNING_CHARGING_RADIUS, LIGHTNING_CHARGING_RADIUS, LIGHTNING_CHARGING_RADIUS),
                entity -> entity.getStack().isIn(CustomTags.ItemTags.LIGHTNING_CHARGEABLE))
            .forEach(item_entity -> {
                var stack = item_entity.getStack();
                if (!stack.getEnchantments().getEnchantments().contains(CHARGE_ENCHANTMENT)) {
                    stack.addEnchantment(CHARGE_ENCHANTMENT, 1);
                    item_entity.setStack(stack);

                    world.getPlayers().forEach(player -> {
                        var distance = Math.max(getPosCompat(player).distanceTo(getPosCompat(item_entity)) - CHARGE_SCREEN_EFFECT_RADIUS_MIN, 0);
                        var strength = CHARGE_SCREEN_MAX_STRENGTH * (charge_screen_effect_radius_diff - distance) / charge_screen_effect_radius_diff;
                        ServerPlayNetworking.send(player, new StartChargeScreenEffect(CHARGE_SCREEN_DURATION, (float) strength));
                    });
                }
        }));
    }
}
