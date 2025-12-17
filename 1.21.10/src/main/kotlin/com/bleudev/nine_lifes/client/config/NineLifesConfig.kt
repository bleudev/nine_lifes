package com.bleudev.nine_lifes.client.config

import com.bleudev.nine_lifes.util.createResourceLocation
import com.google.gson.GsonBuilder
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler
import dev.isxander.yacl3.config.v2.api.SerialEntry
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder
import net.fabricmc.loader.api.FabricLoader

class NineLifesConfig {
    companion object {
        private val HANDLER: ConfigClassHandler<NineLifesConfig> = ConfigClassHandler.createBuilder(NineLifesConfig::class.java)
            .id(createResourceLocation("config"))
            .serializer { GsonConfigSerializerBuilder.create(it)
                .setPath(FabricLoader.getInstance().configDir.resolve("nine_lifes_config.json5"))
                .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                .setJson5(true)
                .build()
            }.build()
        val defaults: NineLifesConfig = HANDLER.defaults()

        fun init() = HANDLER.load()

        var join_message_enabled: Boolean
            get() = HANDLER.instance().join_message_enabled
            set(value) {HANDLER.instance().join_message_enabled = value; HANDLER.save()}
        var heartbeat_enabled: Boolean
            get() = HANDLER.instance().heartbeat_enabled
            set(value) {HANDLER.instance().heartbeat_enabled = value; HANDLER.save()}
        var heart_position: HeartPosition
            get() = HANDLER.instance().heart_position
            set(value) {HANDLER.instance().heart_position = value; HANDLER.save()}
    }

    @SerialEntry
    var join_message_enabled: Boolean = true
    @SerialEntry
    var heartbeat_enabled: Boolean = true
    @SerialEntry
    var heart_position: HeartPosition = HeartPosition.BOTTOM_CENTER

    enum class HeartPosition {
        BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT, TOP_LEFT, TOP_CENTER, TOP_RIGHT
    }
}