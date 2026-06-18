package com.bleudev.nine_lifes.util

import com.bleudev.nine_lifes.MOD_ID

fun config(path: String): String = "yacl3.config.$MOD_ID.$path"
fun rootOption(path: String): String = "category.general.root.option.$path"
fun enumConfig(name: String, entry: String): String = config("enum.$name.$entry")

fun advancement(name: String): String = "advancement.$MOD_ID.$name"
fun advancementDescription(name: String): String = "advancement.$MOD_ID.description.$name"

fun deathScreenRemaining(num: Int): String = "deathScreen.title.remaining.$num"
