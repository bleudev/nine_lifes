package com.bleudev.nine_lifes.config

import com.bleudev.nine_lifes.MOD_ID
import com.bleudev.nine_lifes.api.FriendlyStreamCodec
import com.bleudev.nine_lifes.client.dataSyncedGameConfig
import io.github.xn32.json5k.Json5
import io.github.xn32.json5k.SerialComment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.nio.file.Files

class NLGameConfigManager {
    private constructor()

    private val path = FabricLoader.getInstance().configDir.resolve(MOD_ID).resolve("server.json5")
    private val json = Json5 {
        prettyPrint = true
        encodeDefaults = true
    }
    init {
        init()
    }

    private fun init() {
        if (!Files.exists(path)) {
            val c = NLGameConfig()
            save(c)
        }
    }
    private fun save(c: NLGameConfig, noCheck: Boolean) {
        Files.createDirectories(path.parent)

        val checkError = c.check()
        if (!noCheck && checkError != null) {
            save(checkError.first, true)
            throw IllegalArgumentException(checkError.second)
        }

        val d = json.encodeToString(NLGameConfig.serializer(), c)
        Files.writeString(path, d)
    }
    fun save(config: NLGameConfig) {
        save(config, false)
    }

    fun load(): NLGameConfig {
        val s = Files.readString(path)
        return json.decodeFromString(NLGameConfig.serializer(), s)
    }

    operator fun invoke(transform: NLGameConfig.() -> Unit) {
        val c = load()
        c.transform()
        save(c)
    }
    override fun toString(): String = load().toString()

    @Serializable
    data class NLGameConfig(
        @SerialComment("""
            Wandering stand spawn chance (in percents)
            
            Default: 20
        """)
        @SerialName("wstand_spawn_chance")
        var wStandSpawnChance: Int = 20,
    ) {
        fun with(
            wStandSpawnChance: Int? = null
        ): NLGameConfig = NLGameConfig(
            wStandSpawnChance ?: this.wStandSpawnChance,
        )

        fun check(): Pair<NLGameConfig, String>? {
            if (wStandSpawnChance !in 0..100) {
                return with(20) to "WStand spawn chance must be in 0..100 range. Reset to default value"
            }
            return null
        }

        companion object {
            val STREAM_CODEC: FriendlyStreamCodec<NLGameConfig> = StreamCodec.composite(
                ByteBufCodecs.INT, NLGameConfig::wStandSpawnChance,
                ::NLGameConfig
            )
        }
    }

    companion object {
        private var instance: NLGameConfigManager? = null

        fun getInstance(): NLGameConfigManager {
            if (instance == null) {
                instance = NLGameConfigManager()
            }
            return instance!!
        }

        fun getClientConfig(): NLGameConfig {
            return dataSyncedGameConfig
        }
    }
}