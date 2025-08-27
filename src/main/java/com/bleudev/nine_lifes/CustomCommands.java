package com.bleudev.nine_lifes;

import com.bleudev.nine_lifes.util.LivesUtils;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class CustomCommands {
    public static int nine_lifes(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal("""
            Nine lifes
            
            Developer: bleudev
            Version: 1.0
            """), false);
        return 1;
    }

    public static int nine_lifes_reset(CommandContext<ServerCommandSource> context) {
        LivesUtils.resetLives(context.getSource().getPlayer());
        context.getSource().sendFeedback(() -> Text.literal("Successfully reseted!"), false);
        return 1;
    }
    public static int nine_lifes_reset_player(CommandContext<ServerCommandSource> context) {
        try {
            EntityArgumentType.getOptionalPlayers(context, "players").forEach(LivesUtils::resetLives);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        context.getSource().sendFeedback(() -> Text.literal("Successfully reseted!"), false);
        return 1;
    }

    public static int nine_lifes_set(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal("Syntax: /nl set [lives] <players>"), false);
        return 1;
    }
    public static int nine_lifes_set_lives(CommandContext<ServerCommandSource> context) {
        LivesUtils.setLives(context.getSource().getPlayer(), IntegerArgumentType.getInteger(context, "lives"));
        context.getSource().sendFeedback(() -> Text.literal("Successfully set!"), false);
        return 1;
    }
    public static int nine_lifes_set_lives_players(CommandContext<ServerCommandSource> context) {
        try {
            EntityArgumentType.getOptionalPlayers(context, "players").forEach(player -> {
                LivesUtils.setLives(player, IntegerArgumentType.getInteger(context, "lives"));
            });
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        context.getSource().sendFeedback(() -> Text.literal("Successfully set!"), false);
        return 1;
    }
}
