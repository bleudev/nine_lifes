package com.bleudev.nine_lifes.util

import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import java.net.URI

fun link(text: MutableComponent, uri: String): MutableComponent = text
    .setStyle(text.style.withClickEvent(ClickEvent.OpenUrl(URI.create(uri))))
fun link(uri: String): MutableComponent = literal_link(uri, uri)
fun literal_link(string: String, uri: String): MutableComponent = link(Component.literal(string), uri)