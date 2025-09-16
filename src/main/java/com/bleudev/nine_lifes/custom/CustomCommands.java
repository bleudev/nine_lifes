package com.bleudev.nine_lifes.custom;

import com.bleudev.nine_lifes.util.LivesUtils;
import com.bleudev.nine_lifes.util.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static com.bleudev.nine_lifes.Nine_lifes.*;
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

    public static int nine_lifes(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() ->
            Text.literal(NAME + "\n").formatted(RED).append(
            Text.translatable("commands.nl.text.author", AUTHOR).formatted(WHITE)).append("\n").append(
            Text.translatable("commands.nl.text.version", VERSION).formatted(WHITE)).append("\n\n").append(
            Text.translatable("commands.nl.text.links").formatted(WHITE)).append("\n").append(generateLinks())
            ,false);
        return 1;
    }

    public static int nine_lifes_reset(CommandContext<ServerCommandSource> context) {
        LivesUtils.resetLives(context.getSource().getPlayer());
        context.getSource().sendFeedback(() -> Text.translatable("commands.nl.reset.success"), false);
        return 1;
    }
    public static int nine_lifes_reset_player(CommandContext<ServerCommandSource> context) {
        try {
            EntityArgumentType.getOptionalPlayers(context, "players").forEach(player -> {
                LivesUtils.resetLives(player);
                context.getSource().sendFeedback(() -> Text.translatable("commands.nl.reset.player.success", player.getGameProfile().getName()), false);
            });
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

    public static int nine_lifes_set_lives(CommandContext<ServerCommandSource> context) {
        int lives = IntegerArgumentType.getInteger(context, "lives");
        LivesUtils.setLives(context.getSource().getPlayer(), lives);
        context.getSource().sendFeedback(() -> Text.translatable("commands.nl.set.success", lives), false);
        return 1;
    }
    public static int nine_lifes_set_lives_players(CommandContext<ServerCommandSource> context) {
        try {
            EntityArgumentType.getOptionalPlayers(context, "players").forEach(player -> {
                int lives = IntegerArgumentType.getInteger(context, "lives");
                LivesUtils.setLives(player, lives);
                context.getSource().sendFeedback(() -> Text.translatable("commands.nl.set.player.success", lives, player.getGameProfile().getName()), false);
            });
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

    public static void initialize(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager
            .literal("nl").executes(CustomCommands::nine_lifes)
            .then(CommandManager
                .literal("reset").executes(CustomCommands::nine_lifes_reset)
                    .requires(s -> s.hasPermissionLevel(2))
                .then(CommandManager
                    .argument("players", EntityArgumentType.players())
                    .executes(CustomCommands::nine_lifes_reset_player))
            )
            .then(CommandManager
                .literal("set")
                .requires(s -> s.hasPermissionLevel(2))
                .then(CommandManager
                    .argument("lives", IntegerArgumentType.integer(1, 9))
                        .suggests((context, builder) -> {
                            builder.suggest(1);
                            builder.suggest(3);
                            builder.suggest(5);
                            builder.suggest(9);
                            return CompletableFuture.completedFuture(builder.build());
                        })
                    .executes(CustomCommands::nine_lifes_set_lives)
                    .then(CommandManager
                        .argument("players", EntityArgumentType.players())
                        .executes(CustomCommands::nine_lifes_set_lives_players))
        )));
    }
}
