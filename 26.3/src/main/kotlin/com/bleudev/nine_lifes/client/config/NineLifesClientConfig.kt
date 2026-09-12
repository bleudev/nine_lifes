package com.bleudev.nine_lifes.client.config

import com.bleudev.nine_lifes.LOGGER
import com.bleudev.nine_lifes.client.forceVanillaDeathScreen
import com.bleudev.nine_lifes.util.enumConfig
import dev.isxander.yacl3.api.NameableEnum
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
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
internal var playerAmethysmSelf: Boolean
    get() = configLoad().chargedAmethysm.amethysm.self
    set(new) = configSave(configLoad().apply { chargedAmethysm.amethysm.self = new })

@Serializable
data class NineLifesClientConfig(
    var joinMessage: Boolean = true,
    var heartbeat: Boolean = true,
    var heartPosition: HeartPosition = HeartPosition.BOTTOM_CENTER,
    var healthRendering: HealthRendering = HealthRendering.ALWAYS,
    var deathScreenRemaining: Boolean = true,
    @SerialName("charged_amethysm")
    var chargedAmethysm: ChargedAmethysmData = ChargedAmethysmData(),
) {
    @Serializable
    data class ChargedAmethysmData(
        var enabled: Boolean = true,
        var charged: ChargedData = ChargedData(),
        var amethysm: AmethysmData = AmethysmData(),
    ) {
        @Serializable
        data class ChargedData(var self: Boolean = true, var players: Boolean = true)
        @Serializable
        data class AmethysmData(var self: Boolean = true, var players: Boolean = true)
    }
}

interface TranslatableConfigEnumProvider {
    val names: List<String>
}

enum class HeartPosition : NameableEnum {
    BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT, TOP_LEFT, TOP_CENTER, TOP_RIGHT;

    override fun getDisplayName(): Component = Component.translatable(enumConfig("HeartPosition", name))

    companion object : TranslatableConfigEnumProvider {
        override val names: List<String> = HeartPosition.entries.map { (it.displayName.contents as TranslatableContents).key }
    }
}

enum class HealthRendering(private val forceHardcore: (lifesCount: Int) -> Boolean) : NameableEnum {
    ALWAYS({true}), TRUE({it <= 1}), NEVER({false});

    override fun getDisplayName(): Component = Component.translatable(enumConfig("HealthRendering", name))
    operator fun invoke(lifesCount: Int) = forceHardcore(lifesCount)

    companion object : TranslatableConfigEnumProvider {
        override val names: List<String> = HealthRendering.entries.map { (it.displayName.contents as TranslatableContents).key }
    }
}

private val configPath: Path
    get() = FabricLoader.getInstance().configDir.resolve("nine_lifes.client.config.json")

private val jsonInstance: Json = Json { prettyPrint = true; ignoreUnknownKeys = true }

private fun configLoad(): NineLifesClientConfig {
    try {
        return jsonInstance.decodeFromString<NineLifesClientConfig>(Files.readString(configPath))
    }
    catch (e: Throwable) {
        LOGGER.error("Error while loading config:\n${e.stackTrace.joinToString("\n")}\n\nPlease report about it.")
    }
    return NineLifesClientConfig()
}

private fun configSave(data: NineLifesClientConfig) {
    try {
        Files.writeString(configPath, jsonInstance.encodeToString(data))
    }
    catch (e: Throwable) {
        LOGGER.error("Error while saving config:\n${e.stackTrace.joinToString("\n")}\n\nPlease report about it.")
    }
}

internal fun configInit() {
    try {
        if (!Files.exists(configPath)) Files.writeString(configPath, "{}")
    } catch (e: Throwable) {
        LOGGER.error("Error while initializing config:\n${e.stackTrace.joinToString("\n")}\n\nPlease report about it.")
    }
    configSave(configLoad())
}

