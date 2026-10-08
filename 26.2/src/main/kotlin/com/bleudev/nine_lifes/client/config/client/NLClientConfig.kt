package com.bleudev.nine_lifes.client.config.client

import com.bleudev.nine_lifes.CLIENT_CONFIG_VERSION
import com.bleudev.nine_lifes.LOGGER
import com.bleudev.nine_lifes.MOD_ID
import com.bleudev.nine_lifes.client.forceVanillaDeathScreen
import com.bleudev.nine_lifes.config.game.NLGameConfigManager
import com.bleudev.nine_lifes.util.enumConfig
import dev.isxander.yacl3.api.NameableEnum
import io.github.xn32.json5k.Json5
import io.github.xn32.json5k.SerialComment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.contents.TranslatableContents
import java.nio.file.Files
import java.nio.file.Path


// Public Properties
internal var joinMessageEnabled: Boolean
    get() = configLoad().joinMessage
    set(new) = configSave(configLoad().apply { joinMessage = new } )
internal var heartbeatEnabled: Boolean
    get() = configLoad().heartbeat
    set(new) = configSave(configLoad().apply { heartbeat = new } )
internal var heartPosition: HeartPosition
    get() = configLoad().heartPosition
    set(new) = configSave(configLoad().apply { heartPosition = new } )
internal var healthRendering: HealthRendering
    get() = configLoad().healthRendering
    set(new) = configSave(configLoad().apply { healthRendering = new })
internal var deathScreenRemaining: Boolean
    get() = configLoad().deathScreenRemaining && !forceVanillaDeathScreen
    set(new) = configSave(configLoad().apply { deathScreenRemaining = new })
internal var playersLifesCountEnabled: Boolean
    get() = configLoad().playersLifesCount && NLGameConfigManager.getClientConfig().playersLifesCount
    set(new) = configSave(configLoad().apply { playersLifesCount = new })

internal var playerChargedAmethysm: Boolean
    get() = configLoad().chargedAmethysm.enabled
    set(new) = configSave(configLoad().apply { chargedAmethysm.enabled = new })
internal var playerChargedPlayers: Boolean
    get() = configLoad().chargedAmethysm.charged.players
    set(new) = configSave(configLoad().apply { chargedAmethysm.charged.players = new })
internal var playerChargedSelf: Boolean
    get() = configLoad().chargedAmethysm.charged.self
    set(new) = configSave(configLoad().apply { chargedAmethysm.charged.self = new })
internal var playerAmethysmPlayers: Boolean
    get() = configLoad().chargedAmethysm.amethysm.players
    set(new) = configSave(configLoad().apply { chargedAmethysm.amethysm.players = new })

@Serializable
data class NLClientConfig(
    @SerialComment("""
        Version of client config.
        DO NOT change this manually!!
    """)
    val version: Int = CLIENT_CONFIG_VERSION,

    @SerialName("join_message")
    @SerialComment("""
        Display message with lifes count on join server.
        Default: true
    """)
    var joinMessage: Boolean = true,

    @SerialComment("""
        When true lifes count will beat.
        Default: true
    """)
    var heartbeat: Boolean = true,

    @SerialName("heart_position")
    @SerialComment("""
        Location of lifes count on the screen.
        Default: "BOTTOM_CENTER"
    """)
    var heartPosition: HeartPosition = HeartPosition.BOTTOM_CENTER,

    @SerialName("health_rendering")
    @SerialComment("""
        Controls player health rendering.

        ALWAYS - Always render hardcore hearts.
        TRUE - Only if you have one life.
        NEVER - Vanilla behavior.

        Default: "ALWAYS"
    """)
    var healthRendering: HealthRendering = HealthRendering.ALWAYS,

    @SerialName("death_screen_remaining")
    @SerialComment("""
        Display the number of lifes remaining instead of the "You Died!" message on the death screen.
        Default: true
    """)
    var deathScreenRemaining: Boolean = true,
    @SerialName("players_lifes_count")
    @SerialComment("""
        Show players's lifes count in tab overlay and name tags (nicknames)
        Default: true
    """)
    var playersLifesCount: Boolean = true,
    @SerialName("charged_amethysm")
    @SerialComment("""
        Amethysm/Charged Items
    """)
    var chargedAmethysm: ChargedAmethysmData = ChargedAmethysmData(),
) {
    @Serializable
    data class ChargedAmethysmData(
        @SerialComment("""
            Toggles the effects near players with charged items or Amethysm on or off.
            Useful for quickly disabling everything with a single button.
            Default: true
        """)
        var enabled: Boolean = true,

        @SerialComment("""
            Charged Items
        """)
        var charged: ChargedData = ChargedData(),

        @SerialComment("""
            Amethysm
        """)
        var amethysm: AmethysmData = AmethysmData(),
    ) {
        @Serializable
        data class ChargedData(
            @SerialComment("""
                Show the effect when holding charged items in the inventory.
                Default: true
            """)
            var self: Boolean = true,
            @SerialComment("""
                Show the effect near players with charged items.
                Default: true
            """)
            var players: Boolean = true
        )
        @Serializable
        data class AmethysmData(
            @SerialComment("""
                Show the effect near players with Amethysm.
                Default: true
            """)
            var players: Boolean = true
        )
    }
}

interface TranslatableConfigEnumProvider {
    val names: List<String>
}

enum class HeartPosition : NameableEnum {
    BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT, TOP_LEFT, TOP_CENTER, TOP_RIGHT;

    override fun getDisplayName(): Component = Component.translatable(enumConfig("HeartPosition", name))

    companion object : TranslatableConfigEnumProvider {
        override val names: List<String> = entries.map { (it.displayName.contents as TranslatableContents).key }
    }
}

enum class HealthRendering(private val forceHardcore: (lifesCount: Int) -> Boolean) : NameableEnum {
    ALWAYS({true}), TRUE({it <= 1}), NEVER({false});

    override fun getDisplayName(): Component = Component.translatable(enumConfig("HealthRendering", name))
    operator fun invoke(lifesCount: Int) = forceHardcore(lifesCount)

    companion object : TranslatableConfigEnumProvider {
        override val names: List<String> = entries.map { (it.displayName.contents as TranslatableContents).key }
    }
}

private val oldConfigPath: Path
    get() = FabricLoader.getInstance().configDir.resolve("nine_lifes.client.config.json")
private val configPath: Path
    get() = FabricLoader.getInstance().configDir.resolve(MOD_ID).resolve("client.json5")

private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
private val json5 = Json5 { prettyPrint = true; encodeDefaults = true }

private fun configLoad(): NLClientConfig {
    try {
        return json5.decodeFromString(NLClientConfig.serializer(), Files.readString(configPath))
    }
    catch (e: Throwable) {
        LOGGER.error("Error while loading config:\n${e.stackTrace.joinToString("\n")}\n\nPlease report about it.")
    }
    return NLClientConfig()
}

private fun configSave(data: NLClientConfig) {
    try {
        Files.writeString(configPath, json5.encodeToString(NLClientConfig.serializer(), data))
    }
    catch (e: Throwable) {
        LOGGER.error("Error while saving config:\n${e.stackTrace.joinToString("\n")}\n\nPlease report about it.")
    }
}

private object ClientConfigMigrator {
    // From v1 to v2
    fun migrateOld(config: String): NLClientConfig {
        val obj = json.parseToJsonElement(config)
        if (obj is JsonObject) {
            val new = JsonObject(obj.toMap().mapKeys { (key, _) ->
                if (key == "joinMessage") return@mapKeys "join_message"
                if (key == "heartPosition") return@mapKeys "heart_position"
                if (key == "healthRendering") return@mapKeys "health_rendering"
                if (key == "deathScreenRemaining") return@mapKeys "death_screen_remaining"
                return@mapKeys key
            }).toString()
            val v1 = json5.decodeFromString(NLClientConfig.serializer(), new)
            return migrate(json5.encodeToString(NLClientConfig.serializer(), v1))
        } else {
            return NLClientConfig()
        }
    }

    // From v2 to ...
    fun migrate(config: String): NLClientConfig {
        // No migration now
        return json5.decodeFromString(NLClientConfig.serializer(), config)
    }
}

internal fun configInit() {
    try {
        Files.createDirectories(configPath.parent)
        if (!Files.exists(configPath)) {
            if (Files.exists(oldConfigPath)) {
                val old = Files.readString(oldConfigPath)
                configSave(ClientConfigMigrator.migrateOld(old))
            } else {
                configSave(NLClientConfig())
            }

        }
    } catch (e: Throwable) {
        LOGGER.error("Error while initializing config:\n${e.stackTrace.joinToString("\n")}\n\nPlease report about it.")
    }
}

