package com.bleudev.nine_lifes;

import com.bleudev.nine_lifes.custom.*;
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
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.explosion.EntityExplosionBehavior;

import static com.bleudev.nine_lifes.compat.VersionCompat.getPosCompat;
import static com.bleudev.nine_lifes.util.ComponentUtils.item_ensure_custom_foods;
import static com.bleudev.nine_lifes.util.ComponentUtils.should_update_amethyst_shard;

public class Nine_lifes implements ModInitializer {
    public static final String MOD_ID = "nine_lifes";
    public static final String NAME = "Nine lifes";
    public static final String AUTHOR = "bleudev";
    public static final String VERSION = "1.8";
    public static final String GITHUB_LINK = "https://github.com/bleudev/nine_lifes";
    public static final String MODRINTH_LINK = "https://modrinth.com/mod/nine_lifes";

    @Override
    public void onInitialize() {
        Packets.initialize();
        CustomEffects.initialize();
        CustomConsumeEffectTypes.initialize();
        CustomEnchantments.initialize();
        CustomPotions.initialize();
        CustomEntities.initialize();
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(Potions.WATER, Items.AMETHYST_SHARD, CustomPotions.AMETHYSM);
        });
        ServerPlayerEvents.JOIN.register(player -> {
            if ((!player.isSpectator()) && (LivesUtils.getLives(player) == 0))
                LivesUtils.resetLives(player);
            ServerPlayNetworking.send(player, new UpdateCenterHeart(LivesUtils.getLives(player)));
            ServerPlayNetworking.send(player, new JoinMessage(LivesUtils.getLives(player)));
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
                                    entity.getX(), entity.getY(), entity.getZ(),
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
                world.getEntitiesByType(EntityType.WIND_CHARGE, ignored -> true).forEach(wind_charge -> {
                    WindChargeTickFeatures.do_for(world, wind_charge);
                });
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CustomCommands.initialize(dispatcher);
        });
    }

    private void tryChargeItems(ServerWorld world) {
        var lightning_charging_radius = 1;
        var charge_screen_effect_radius_min = 3;
        var charge_screen_effect_radius_max = 20;
        var charge_screen_effect_radius_diff = charge_screen_effect_radius_max - charge_screen_effect_radius_min;
        var charge_screen_max_strength = 0.5;
        var charge_screen_duration = 6;

        final var CHARGE_ENCHANTMENT = CustomEnchantments.getEntry(world.getRegistryManager(), CustomEnchantments.CHARGE);
        world.getEntitiesByType(EntityType.LIGHTNING_BOLT, ignored -> true).forEach(lightning -> {
            world.getEntitiesByType(EntityType.ITEM,
                    Box.of(getPosCompat(lightning), lightning_charging_radius, lightning_charging_radius, lightning_charging_radius),
                    entity -> entity.getStack().isIn(CustomTags.ItemTags.LIGHTNING_CHARGEABLE)
            ).forEach(item_entity -> {
                var stack = item_entity.getStack();
                if (!stack.getEnchantments().getEnchantments().contains(CHARGE_ENCHANTMENT)) {
                    stack.addEnchantment(CHARGE_ENCHANTMENT, 1);
                    item_entity.setStack(stack);

                    world.getPlayers().forEach(player -> {
                        var distance = Math.max(getPosCompat(player).distanceTo(getPosCompat(item_entity)) - charge_screen_effect_radius_min, 0);
                        var strength = charge_screen_max_strength * (charge_screen_effect_radius_diff - distance) / charge_screen_effect_radius_diff;
                        ServerPlayNetworking.send(player, new StartChargeScreenEffect(charge_screen_duration, (float) strength));
                    });
                }
            });
        });
    }
}
