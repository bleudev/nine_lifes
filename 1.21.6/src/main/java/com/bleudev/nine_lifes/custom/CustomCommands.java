package com.bleudev.nine_lifes.custom;

import com.bleudev.nine_lifes.util.LivesUtils;
import com.bleudev.nine_lifes.util.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.PermissionLevelSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import static com.bleudev.nine_lifes.NineLifesConst.*;
import static net.minecraft.util.Formatting.*;

public class CustomCommands {
    private record LinkData(String string, String link, Formatting color) {}

    private static final ArrayList<LinkData> links = new ArrayList<>();

    static {
        links.add(new LinkData("Github", GITHUB_LINK, GRAY));
        links.add(new LinkData("Modrinth", MODRINTH_LINK, GREEN));
    }

    private static MutableText generateLinks() {
        MutableText res = Text.empty();
        for (int i = 0; i < links.size(); i++) {
            var link = links.get(i);
            res = res.append(Text.literal("- ").formatted(WHITE));
            res = res.append(TextUtils.literal_link(link.string, link.link).formatted(link.color));
            if (i < links.size() - 1)
                res = res.append("\n");
        }
        return res;
    }

    public static Collection<ServerPlayerEntity> getPlayers(@NotNull CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return EntityArgumentType.getOptionalPlayers(context, "players");
    }

    public static int nl(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() ->
            Text.literal(NAME + "\n").formatted(RED).append(
            Text.translatable("commands.nl.text.author", AUTHOR).formatted(WHITE)).append("\n").append(
            Text.translatable("commands.nl.text.version", VERSION).formatted(WHITE)).append("\n\n").append(
            Text.translatable("commands.nl.text.links").formatted(WHITE)).append("\n").append(generateLinks())
            ,false);
        return 1;
    }

    public static int nl_reset(CommandContext<ServerCommandSource> context) {
        var player = context.getSource().getPlayer();
        if (player == null) {
            context.getSource().sendFeedback(() -> Text.translatable("commands.nl.reset.not_a_player"), false);
            return -1;
        }
        LivesUtils.resetLives(player);
        context.getSource().sendFeedback(() -> Text.translatable("commands.nl.reset.success"), false);
        return 1;
    }
    public static int nl_reset_player(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        getPlayers(context).forEach(player -> {
            LivesUtils.resetLives(player);
            context.getSource().sendFeedback(() -> Text.translatable("commands.nl.reset.player.success", player.getGameProfile().getName()), false);
        });
        return 1;
    }

    public static int nl_set_lives(CommandContext<ServerCommandSource> context) {
        int lives = IntegerArgumentType.getInteger(context, "lives");
        var player = context.getSource().getPlayer();
        if (player == null) {
            context.getSource().sendFeedback(() -> Text.translatable("commands.nl.set.not_a_player"), false);
            return -1;
        }
        LivesUtils.setLives(player, lives);
        context.getSource().sendFeedback(() -> Text.translatable("commands.nl.set.success", lives), false);
        return 1;
    }
    public static int nl_set_lives_players(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        int lives = IntegerArgumentType.getInteger(context, "lives");
        getPlayers(context).forEach(player -> {
            LivesUtils.setLives(player, lives);
            context.getSource().sendFeedback(() -> Text.translatable("commands.nl.set.player.success", lives, player.getGameProfile().getName()), false);
        });
        return 1;
    }

    public static int nl_revive(CommandContext<ServerCommandSource> context) {
        var player = context.getSource().getPlayer();
        if (player == null) {
            context.getSource().sendFeedback(() -> Text.translatable("commands.nl.revive.not_a_player"), false);
            return -1;
        }
        LivesUtils.revive(player);
        context.getSource().sendFeedback(() -> Text.translatable("commands.nl.revive.success"), false);
        return 1;
    }
    public static int nl_revive_players(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        getPlayers(context).forEach(player -> {
            LivesUtils.revive(player);
            context.getSource().sendFeedback(() -> Text.translatable("commands.nl.revive.player.success", player.getGameProfile().getName()), false);
        });
        return 1;
    }

    public static void initialize(CommandDispatcher<ServerCommandSource> dispatcher) {
        var players_argument = CommandManager
                .argument("players", EntityArgumentType.players());
        dispatcher.register(CommandManager
            .literal("nl")
            .executes(CustomCommands::nl)
            .then(CommandManager
                .literal("reset")
                .requires(PermissionLevelSource::hasElevatedPermissions)
                .executes(CustomCommands::nl_reset)
                .then(players_argument
                    .executes(CustomCommands::nl_reset_player)))
            .then(CommandManager
                .literal("set")
                .requires(PermissionLevelSource::hasElevatedPermissions)
                .then(CommandManager
                    .argument("lives", IntegerArgumentType.integer(1, 9))
                    .suggests((context, builder) -> builder
                        .suggest(1)
                        .suggest(3)
                        .suggest(5)
                        .suggest(9)
                        .buildFuture())
                    .executes(CustomCommands::nl_set_lives)
                    .then(players_argument
                        .executes(CustomCommands::nl_set_lives_players))))
            .then(CommandManager
                .literal("revive")
                .requires(PermissionLevelSource::hasElevatedPermissions)
                .executes(CustomCommands::nl_revive)
                .then(players_argument
                    .executes(CustomCommands::nl_revive_players)))
        );
    }
}
