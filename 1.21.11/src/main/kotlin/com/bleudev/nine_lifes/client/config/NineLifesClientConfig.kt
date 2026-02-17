package com.bleudev.nine_lifes.client.config

import com.bleudev.nine_lifes.LOGGER
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Files
import java.nio.file.Path

// Public Properties
var joinMessageEnabled: Boolean
    get() = configLoad().joinMessage
    set(new) = configSave(configLoad().apply { joinMessage = new } )
var heartbeatEnabled: Boolean
    get() = configLoad().heartbeat
    set(new) = configSave(configLoad().apply { heartbeat = new } )
var heartPosition: HeartPosition
    get() = configLoad().heartPosition
    set(new) = configSave(configLoad().apply { heartPosition = new } )
var lowLifesRedSkyEnabled: Boolean
    get() = configLoad().lowLifesRedSky
    set(new) = configSave(configLoad().apply { lowLifesRedSky = new } )

@Serializable
data class NineLifesClientConfig(
    var joinMessage: Boolean = true,
    var heartbeat: Boolean = true,
    var heartPosition: HeartPosition = HeartPosition.BOTTOM_CENTER,
    var lowLifesRedSky: Boolean = true
)

enum class HeartPosition {
    BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT, TOP_LEFT, TOP_CENTER, TOP_RIGHT
}

private val configPath: Path
    get() = FabricLoader.getInstance().configDir.resolve("nine_lifes.client.config.json")

private val jsonInstance: Json = Json{prettyPrint = true}

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

fun configInit() {
    try {
        if (!Files.exists(configPath)) Files.writeString(configPath, "{}")
    } catch (e: Throwable) {
        LOGGER.error("Error while initializing config:\n${e.stackTrace.joinToString("\n")}\n\nPlease report about it.")
    }
    configSave(configLoad())
}

