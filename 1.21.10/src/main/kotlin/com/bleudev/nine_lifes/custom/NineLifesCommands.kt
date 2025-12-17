package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.*
import com.bleudev.nine_lifes.util.*
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.selector.EntitySelector
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

object NineLifesCommands {
    private val links: List<Pair<String, LinkData>> = listOf(
        "Github" to LinkData(GITHUB_LINK, ChatFormatting.GRAY),
        "Modrinth" to LinkData(MODRINTH_LINK, ChatFormatting.GREEN),
    )

    private fun generateLinks(): MutableComponent {
        var res = Component.empty()
        for (i in links.indices) {
            val link = links[i]
            res = res.append(Component.literal("- ").withStyle(ChatFormatting.WHITE))
            res = res.append(literal_link(link.first, link.second.link).withStyle(link.second.style))
            if (i < links.size - 1) res = res.append("\n")
        }
        return res
    }

    val playersArgument: RequiredArgumentBuilder<CommandSourceStack, EntitySelector> = Commands
        .argument("players", EntityArgument.players())
    val lifesArgument: RequiredArgumentBuilder<CommandSourceStack, Int> = Commands
        .argument("lifes", IntegerArgumentType.integer(1, 9))

    fun nl(context: CommandContext<CommandSourceStack>): Int {
        context.getSource().sendSuccess({
            Component.literal(NAME + "\n").withStyle(ChatFormatting.RED).append(
                Component.translatable("commands.nl.text.author", AUTHOR).withStyle(ChatFormatting.WHITE)
            ).append("\n").append(
                Component.translatable("commands.nl.text.version", VERSION).withStyle(ChatFormatting.WHITE)
            ).append("\n\n").append(
                Component.translatable("commands.nl.text.links").withStyle(ChatFormatting.WHITE)
            ).append("\n").append(generateLinks())
        }, false)
        return 1
    }

    fun nlReset(context: CommandContext<CommandSourceStack>): Int {
        val player = context.getSource().player.requireNotNullOr {
            context.getSource().sendFailure(Component.translatable("commands.nl.reset.not_a_player"))
        }; if (player == null) return -1
        player.resetLifes()
        context.getSource().sendSuccess({ Component.translatable("commands.nl.reset.success") }, false)
        return 1
    }

    fun nlResetPlayers(ctx: CommandContext<CommandSourceStack>): Int {
        EntityArgument.getOptionalPlayers(ctx, playersArgument.name).forEach { player ->
            player.resetLifes()
            ctx.getSource().sendSuccess({ Component.translatable("commands.nl.reset.player.success", player.gameProfile.name) }, false)
        }
        return 1
    }

    fun nlSetLifes(ctx: CommandContext<CommandSourceStack>): Int {
        val lifes = IntegerArgumentType.getInteger(ctx, lifesArgument.name)
        val player = ctx.getSource().player.requireNotNullOr {
            ctx.getSource().sendFailure(Component.translatable("commands.nl.set.not_a_player"))
        }; if (player == null) return -1
        player.setLifes(lifes)
        ctx.getSource().sendSuccess({ Component.translatable("commands.nl.set.success", lifes) }, false)
        return 1
    }

    fun nlSetLifesPlayers(ctx: CommandContext<CommandSourceStack>): Int {
        val lifes = IntegerArgumentType.getInteger(ctx, lifesArgument.name)
        EntityArgument.getOptionalPlayers(ctx, playersArgument.name).forEach { player ->
            player.setLifes(lifes)
            ctx.getSource().sendSuccess({ Component.translatable("commands.nl.set.player.success", lifes, player.gameProfile.name) }, false)
        }
        return 1
    }

    fun nlRevive(ctx: CommandContext<CommandSourceStack>): Int {
        val player = ctx.getSource().player.requireNotNullOr {
            ctx.getSource().sendFailure(Component.translatable("commands.nl.revive.not_a_player"))
        }; if (player == null) return -1
        player.revive()
        ctx.getSource().sendSuccess({ Component.translatable("commands.nl.revive.success") }, false)
        return 1
    }

    fun nlRevivePlayers(ctx: CommandContext<CommandSourceStack>): Int {
        EntityArgument.getOptionalPlayers(ctx, playersArgument.name).forEach { player ->
            player.revive()
            ctx.getSource().sendSuccess({
                Component.translatable("commands.nl.revive.player.success", player.gameProfile.name)
            }, false)
        }
        return 1
    }

    fun initialize() {
        CommandRegistrationCallback.EVENT.register { d, _, _ ->
            d.register(Commands.literal("nl")
                .executes(::nl)
                .then(Commands.literal("reset")
                    .requiresAdmin()
                    .executes(::nlReset)
                    .then(playersArgument
                        .executes(::nlResetPlayers))
                )
                .then(Commands.literal("set")
                    .requiresAdmin()
                    .then(lifesArgument
                        .suggests { _, b -> b.suggestMany(1, 5, 9).buildFuture() }
                        .executes(::nlSetLifes)
                        .then(playersArgument
                            .executes(::nlSetLifesPlayers)))
                )
                .then(Commands.literal("revive")
                    .requiresAdmin()
                    .executes(::nlRevive)
                    .then(playersArgument
                        .executes(::nlRevivePlayers)))
            )
        }
    }

    private class LinkData(val link: String, val style: ChatFormatting)
}