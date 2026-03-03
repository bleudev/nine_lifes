package com.bleudev.nine_lifes.util

fun config(path: String): String = "yacl3.config.nine_lifes.$path"
fun enumConfig(name: String, entry: String): String = config("enum.$name.$entry")
