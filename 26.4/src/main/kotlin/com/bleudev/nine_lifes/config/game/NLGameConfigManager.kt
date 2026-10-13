package com.bleudev.nine_lifes.config.game

import com.bleudev.nine_lifes.GAME_CONFIG_VERSION
import com.bleudev.nine_lifes.MOD_ID
import com.bleudev.nine_lifes.api.FriendlyStreamCodec
import com.bleudev.nine_lifes.client.dataSyncedGameConfig
import io.github.xn32.json5k.Json5
import io.github.xn32.json5k.SerialComment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.chat.Component
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
            throw checkError.second
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
            Version of game config.
            DO NOT change this manually!!
        """)
        val version: Int = GAME_CONFIG_VERSION,
        @SerialComment("""
            Disable wandering armor stands entirely
            This means that all wandering armor stand will kill after turning on this property and will not be longer available to spawn.
            Default: false
        """)
        var disableWStands: Boolean = false,
        @SerialComment("""
            Wandering stand spawn chance (in percents).
            Default: 20
        """)
        @SerialName("wstand_spawn_chance")
        var wStandSpawnChance: Int = 20,
        @SerialComment("""
            Display player lifes count in the tab / nicknames.
            Default: true
        """)
        @SerialName("players_lifes_count")
        var playersLifesCount: Boolean = true,
    ) {
        fun with(
            version: Int? = null,
            disableWStands: Boolean? = null,
            wStandSpawnChance: Int? = null,
            playersLifesCount: Boolean? = null,
        ): NLGameConfig = NLGameConfig(
            version ?: this.version,
            disableWStands ?: this.disableWStands,
            wStandSpawnChance ?: this.wStandSpawnChance,
            playersLifesCount ?: this.playersLifesCount,
        )

        fun check(): Pair<NLGameConfig, GameConfigCheckException>? {
            var bl = false
            var exc = GameConfigCheckException.empty()
            var res = with() // Copy

            if (version !in 1..GAME_CONFIG_VERSION) {
                bl = true
                exc = exc.chain(GameConfigCheckException.of(Component.translatable("config.game.check.exception.version", version)))
                res = res.with(version = GAME_CONFIG_VERSION)
            }
            if (wStandSpawnChance !in 0..100) {
                bl = true
                exc = exc.chain(GameConfigCheckException.of(Component.translatable("config.game.check.exception.wstand_spawn_chance", wStandSpawnChance)))
                res = res.with(wStandSpawnChance = 20)
            }
            return if (bl) res to exc else null
        }

        companion object {
            val STREAM_CODEC: FriendlyStreamCodec<NLGameConfig> = StreamCodec.composite(
                ByteBufCodecs.INT, NLGameConfig::version,
                ByteBufCodecs.BOOL, NLGameConfig::disableWStands,
                ByteBufCodecs.INT, NLGameConfig::wStandSpawnChance,
                ByteBufCodecs.BOOL, NLGameConfig::playersLifesCount,
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