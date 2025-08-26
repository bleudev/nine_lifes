package com.bleudev.nine_lifes;

import com.bleudev.nine_lifes.util.LivesUtils;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class CustomCommands {
    public static int nine_lifes(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal("""
            Nine lifes
            
            Developer: bleudev
            Version: idk
            """), false);
        return 1;
    }
    public static int nine_lifes_reset(CommandContext<ServerCommandSource> context) {
        LivesUtils.resetLives(context.getSource().getPlayer());
        context.getSource().sendFeedback(() -> Text.literal("Successfully reseted!"), false);
        return 1;
    }
}
