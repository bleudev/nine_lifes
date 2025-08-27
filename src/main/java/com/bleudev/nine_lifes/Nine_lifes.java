package com.bleudev.nine_lifes;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;

public class Nine_lifes implements ModInitializer {
    public static final String MOD_ID = "nine_lifes";
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager
                .literal("nl").executes(CustomCommands::nine_lifes)
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
