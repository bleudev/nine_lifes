package com.bleudev.nine_lifes;

import com.bleudev.nine_lifes.custom.*;
import com.bleudev.nine_lifes.interfaces.mixin.LivingEntityCustomInterface;
import com.bleudev.nine_lifes.networking.Packets;
import com.bleudev.nine_lifes.networking.payloads.JoinMessagePayload;
import com.bleudev.nine_lifes.networking.payloads.UpdateCenterHeartPayload;
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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.explosion.EntityExplosionBehavior;

import static com.bleudev.nine_lifes.util.ComponentUtils.item_ensure_custom_foods;
import static com.bleudev.nine_lifes.util.ComponentUtils.should_update_amethyst_shard;

public class Nine_lifes implements ModInitializer {
    public static final String MOD_ID = "nine_lifes";
    public static final String NAME = "Nine lifes";
    public static final String AUTHOR = "bleudev";
    public static final String VERSION = "1.4.1";
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
            if (player.getGameMode() == GameMode.SURVIVAL)
                if (LivesUtils.getLives(player) == 0)
                    LivesUtils.setLives(player, 9);
            ServerPlayNetworking.send(player, new UpdateCenterHeartPayload(LivesUtils.getLives(player)));
            ServerPlayNetworking.send(player, new JoinMessagePayload(LivesUtils.getLives(player)));
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            // Custom food (amethyst shard)
            for (var player: server.getPlayerManager().getPlayerList())
                ComponentUtils.player_ensure_custom_foods(player);

            // Find all amethyst shard item entities in `find_radius`
            double find_radius = 20;
            for (var world: server.getWorlds())
                for (var player: world.getPlayers()) {
                    var box = Box.of(player.getPos(), find_radius, find_radius, find_radius);
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
                            inter.nine_lifes$setDamageTicks(-1);
                            world.createExplosion(
                                    entity, CustomDamageTypes.of(world, CustomDamageTypes.CHARGED_AMETHYST_DAMAGE_TYPE), new EntityExplosionBehavior(entity),
                                    entity.getX(), entity.getY(), entity.getZ(),
                                    7f, true, World.ExplosionSourceType.MOB,
                                    ParticleTypes.LARGE_SMOKE, ParticleTypes.LARGE_SMOKE,
                                    SoundEvents.ENTITY_GENERIC_EXPLODE
                            );
                            entity.damage(world,
                                CustomDamageTypes.of(entity, CustomDamageTypes.CHARGED_AMETHYST_DAMAGE_TYPE),
                                1000f
                            );
                        } else inter.nine_lifes$setDamageTicks(damage_ticks - 1);
                    });
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
}
