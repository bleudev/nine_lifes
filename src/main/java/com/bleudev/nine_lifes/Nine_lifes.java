package com.bleudev.nine_lifes;

import com.bleudev.nine_lifes.custom.CustomConsumeEffectTypes;
import com.bleudev.nine_lifes.custom.CustomEffects;
import com.bleudev.nine_lifes.custom.CustomEnchantments;
import com.bleudev.nine_lifes.custom.CustomPotions;
import com.bleudev.nine_lifes.networking.Packets;
import com.bleudev.nine_lifes.networking.payloads.UpdateCenterHeartPayload;
import com.bleudev.nine_lifes.util.ComponentUtils;
import com.bleudev.nine_lifes.util.LivesUtils;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.command.PermissionLevelSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.math.Box;
import net.minecraft.world.GameMode;

import static com.bleudev.nine_lifes.util.ComponentUtils.item_ensure_custom_foods;
import static com.bleudev.nine_lifes.util.ComponentUtils.should_update_amethyst_shard;

public class Nine_lifes implements ModInitializer {
    public static final String MOD_ID = "nine_lifes";
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
            dispatcher.register(CommandManager
                .literal("nl").executes(CustomCommands::nine_lifes)
                .requires(PermissionLevelSource::hasElevatedPermissions)
                .then(CommandManager
                    .literal("reset").executes(CustomCommands::nine_lifes_reset)
                    .then(CommandManager
                        .argument("players", EntityArgumentType.players())
                        .executes(CustomCommands::nine_lifes_reset_player))
                )
                .then(CommandManager
                    .literal("set").executes(CustomCommands::nine_lifes_set)
                    .then(CommandManager
                        .argument("lives", IntegerArgumentType.integer(1, 9))
                        .executes(CustomCommands::nine_lifes_set_lives)
                        .then(CommandManager
                            .argument("players", EntityArgumentType.players())
                            .executes(CustomCommands::nine_lifes_set_lives_players))
                    )
                )
            );
        });
    }
}
