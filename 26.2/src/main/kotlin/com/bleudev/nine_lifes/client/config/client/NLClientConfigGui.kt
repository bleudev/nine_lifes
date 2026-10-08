package com.bleudev.nine_lifes.client.config.client

import com.bleudev.nine_lifes.MOD_ID
import com.bleudev.nine_lifes.client.config.*
import dev.isxander.yacl3.dsl.YetAnotherConfigLib
import dev.isxander.yacl3.dsl.descriptionBuilder
import net.minecraft.client.gui.screens.Screen

private var cachedJoinMessage: Boolean? = null
private var cachedHeartbeat: Boolean? = null
private var cachedHeartPosition: HeartPosition? = null
private var cachedHealthRendering: HealthRendering? = null
private var cachedDeathScreenRemaining: Boolean? = null
private var cachedPlayersLifesCountEnabled: Boolean? = null
private var cachedChargedAmethysm: Boolean? = null
private var cachedChargedSelf: Boolean? = null
private var cachedChargedPlayers: Boolean? = null
private var cachedAmethysmPlayers: Boolean? = null

fun generateGuiConfigScreen(parent: Screen?): Screen = YetAnotherConfigLib(MOD_ID) {
    categories.register("general") {
        rootOptions.register("join_message") {
            binding(true, ::joinMessageEnabled)
            yesNoFormat()
            cachePending(::cachedJoinMessage::set)
            descriptionBuilder {
                addDefaultText(1)
                localisedConfigImage("join_message") {cachedJoinMessage ?: joinMessageEnabled}
            }
        }
        rootOptions.register("heartbeat") {
            binding(true, ::heartbeatEnabled)
            yesNoFormat()
            cachePending(::cachedHeartbeat::set)
            descriptionBuilder {
                addDefaultText(1)
                framedConfigImage("heartbeat", 20, 20, { cachedHeartbeat ?: heartbeatEnabled }, true)
            }
        }
        rootOptions.register("heart_position") {
            binding(HeartPosition.BOTTOM_CENTER, ::heartPosition)
            enumFormat()
            cachePending(::cachedHeartPosition::set)
            descriptionBuilder {
                addDefaultText(1)
                enumConfigImage("heart_position") {cachedHeartPosition ?: heartPosition}
            }
        }
        rootOptions.register("health_rendering") {
            binding(HealthRendering.ALWAYS, ::healthRendering)
            enumFormat()
            cachePending(::cachedHealthRendering::set)
            descriptionBuilder {
                addDefaultText(1)
                customImage(HealthRenderingImageRenderer {cachedHealthRendering ?: healthRendering})
            }
        }
        rootOptions.register("death_screen_remaining") {
            binding(true, ::deathScreenRemaining)
            yesNoFormat()
            cachePending(::cachedDeathScreenRemaining::set)
            descriptionBuilder {
                addDefaultText(1)
                fullLocalisedConfigImage("death_screen_remaining") {cachedDeathScreenRemaining ?: deathScreenRemaining}
            }
        }
        rootOptions.register("players_lifes_count") {
            binding(true, ::playersLifesCountEnabled)
            yesNoFormat()
            cachePending(::cachedPlayersLifesCountEnabled::set)
            descriptionBuilder {
                addDefaultText(1)
                conditionConfigImage("players_lifes_count") { cachedPlayersLifesCountEnabled ?: playersLifesCountEnabled }
            }
        }
        categories.register("charged_amethysm") {
            rootOptions.register("enabled") {
                binding(true, ::playerChargedAmethysm)
                yesNoFormat()
                cachePending(::cachedChargedAmethysm::set)
                addListener { option, _ ->
                    if (thisCategory.isDone) {
                        thisCategory.get().groups().flatMap { it.options() }.forEach {
                            if (it.name() != option.name()) {
                                it.setAvailable(cachedChargedAmethysm ?: playerChargedAmethysm)
                            }
                        }
                    }
                }
                descriptionBuilder {
                    addDefaultText(1)
                }
            }
            groups.register("charged") {
                options.register("self") {
                    binding(true, ::playerChargedSelf)
                    yesNoFormat()
                    cachePending(::cachedChargedSelf::set)
                    available(playerChargedAmethysm)
                    descriptionBuilder {
                        addDefaultText(1)
                        conditionConfigImage("charged_self") { cachedChargedSelf ?: playerChargedSelf }
                    }
                }
                options.register("players") {
                    binding(true, ::playerChargedPlayers)
                    yesNoFormat()
                    cachePending(::cachedChargedPlayers::set)
                    available(playerChargedAmethysm)
                    descriptionBuilder {
                        addDefaultText(1)
                        conditionConfigImage("charged_players") { cachedChargedPlayers ?: playerChargedPlayers }
                    }
                }
            }
            groups.register("amethysm") {
                options.register("players") {
                    binding(true, ::playerAmethysmPlayers)
                    yesNoFormat()
                    cachePending(::cachedAmethysmPlayers::set)
                    available(playerChargedAmethysm)
                    descriptionBuilder {
                        addDefaultText(1)
                        conditionConfigImage("amethysm_players") { cachedAmethysmPlayers ?: playerAmethysmPlayers }
                    }
                }
            }
        }
    }
}.generateScreen(parent)
