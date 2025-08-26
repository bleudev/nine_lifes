package com.bleudev.nine_lifes;

import com.bleudev.nine_lifes.util.LivesUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
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
                ));
        });
    }
}
