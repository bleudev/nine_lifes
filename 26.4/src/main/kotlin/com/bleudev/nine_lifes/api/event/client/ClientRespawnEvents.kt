package com.bleudev.nine_lifes.api.event.client

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.Minecraft

@Environment(EnvType.CLIENT)
object ClientRespawnEvents {
    val RESPAWN: Event<ClientRespawn> = EventFactory.createArrayBacked(ClientRespawn::class.java) {
        { mc -> for (i in it) i(mc) }
    }

    typealias ClientRespawn = (mc: Minecraft) -> Unit
}