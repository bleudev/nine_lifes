package com.bleudev.nine_lifes.util

import com.bleudev.nine_lifes.MOD_ID
import kotlin.math.min

class ConfigTranslationKeyBuilder private constructor(private val current: Array<String?>) {
    fun category(category: String = "general"): ConfigTranslationKeyBuilder =
        update(arrayOfNulls<String>(2).also { it[1] = "category.$category" })
    fun root(): ConfigTranslationKeyBuilder =
        update(arrayOfNulls<String>(3).also { it[2] = "root" })
    fun group(group: String): ConfigTranslationKeyBuilder =
        update(arrayOfNulls<String>(3).also { it[2] = "group.$group" })
    fun option(option: String): ConfigTranslationKeyBuilder =
        update(arrayOfNulls<String>(4).also { it[3] = "option.$option" })
    fun description(): ConfigTranslationKeyBuilder =
        update(arrayOfNulls<String>(5).also { it[4] = "description" })
    fun additional(path: String): ConfigTranslationKeyBuilder =
        update(arrayOfNulls<String>(DEFAULT_SIZE).also { it[DEFAULT_SIZE-1] = path })

    private fun update(patch: Array<String?>): ConfigTranslationKeyBuilder {
        val new = current.copyOf()
        for (i in 0..<min(new.size, patch.size)) {
            if (patch[i] != null) {
                new[i] = patch[i]
            }
        }
        return ConfigTranslationKeyBuilder(new)
    }
    fun update(patch: ConfigTranslationKeyBuilder): ConfigTranslationKeyBuilder = update(patch.current)
    fun build(): String = current.reduceOrNull { acc, string -> if (string == null) acc else if (acc == null) "" else "$acc.$string" } ?: ""

    companion object {
        private const val DEFAULT_SIZE = 999
        fun of(id: String): ConfigTranslationKeyBuilder {
            val current = arrayOfNulls<String>(DEFAULT_SIZE)
            current[0] = "yacl3.config.$id"
            return ConfigTranslationKeyBuilder(current)
        }
        fun default(): ConfigTranslationKeyBuilder =
            of(MOD_ID)
    }
}

fun config(path: String): String = "yacl3.config.$MOD_ID.$path"
fun enumConfig(name: String, entry: String): String = config("enum.$name.$entry")

fun advancement(name: String): String = "advancement.$MOD_ID.$name"
fun advancementDescription(name: String): String = "advancement.$MOD_ID.description.$name"

fun deathScreenRemaining(num: Int): String = "deathScreen.title.remaining.$num"
