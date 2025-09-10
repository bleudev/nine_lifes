package com.bleudev.nine_lifes;

import com.bleudev.nine_lifes.custom.*;
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
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.util.math.Box;
import net.minecraft.world.GameMode;

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
            for (var player: server.getPlayerManager().getPlayerList())
                ComponentUtils.player_ensure_custom_foods(player);

            // Find all amethyst shard item entities in `find_radius`
            double find_radius = 20;
            for (var world: server.getWorlds())
                for (var player: world.getPlayers()) {
                    world.getEntitiesByType(
                        EntityType.ITEM,
                        Box.of(player.getPos(), find_radius, find_radius, find_radius),
                        entity -> entity.getStack().isOf(Items.AMETHYST_SHARD) &&
                                         should_update_amethyst_shard(entity.getStack())
                    ).forEach(entity -> entity.setStack(item_ensure_custom_foods(entity.getStack())));
                }
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CustomCommands.initialize(dispatcher);
        });
    }
}
