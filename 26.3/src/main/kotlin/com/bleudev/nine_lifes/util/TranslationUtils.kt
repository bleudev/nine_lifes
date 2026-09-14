package com.bleudev.nine_lifes.util

import com.bleudev.nine_lifes.MOD_ID

class ConfigTranslationBuilder private constructor(private val current: Array<String?>) {
    fun category(category: String = "general"): ConfigTranslationBuilder {
        current[1] = "category.$category"
        return ConfigTranslationBuilder(current)
    }
    fun root(): ConfigTranslationBuilder {
        current[2] = "root"
        return ConfigTranslationBuilder(current)
    }
    fun group(group: String): ConfigTranslationBuilder {
        current[2] = "group.$group"
        return ConfigTranslationBuilder(current)
    }
    fun option(option: String): ConfigTranslationBuilder {
        current[3] = "option.$option"
        return ConfigTranslationBuilder(current)
    }
    fun description(): ConfigTranslationBuilder {
        current[4] = "description"
        return ConfigTranslationBuilder(current)
    }

    private fun update(patch: Array<String?>): ConfigTranslationBuilder {
        for (i in current.indices) {
            if (patch[i] != null) {
                current[i] = patch[i]
            }
        }
        return ConfigTranslationBuilder(current)
    }
    fun update(patch: ConfigTranslationBuilder): ConfigTranslationBuilder = update(patch.current)
    fun build(): String = current.reduceOrNull { acc, string -> if (string == null) acc else if (acc == null) "" else "$acc.$string" } ?: ""

    companion object {
        fun of(id: String): ConfigTranslationBuilder {
            val current = arrayOfNulls<String>(5)
            current[0] = "yacl3.config.$id"
            return ConfigTranslationBuilder(current)
        }
        fun default(): ConfigTranslationBuilder =
            of(MOD_ID)
    }
}

fun config(path: String): String = "yacl3.config.$MOD_ID.$path"
fun rootOption(path: String): String = "category.general.root.option.$path"
fun enumConfig(name: String, entry: String): String = config("enum.$name.$entry")

fun advancement(name: String): String = "advancement.$MOD_ID.$name"
fun advancementDescription(name: String): String = "advancement.$MOD_ID.description.$name"

fun deathScreenRemaining(num: Int): String = "deathScreen.title.remaining.$num"
