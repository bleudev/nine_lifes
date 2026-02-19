package com.bleudev.nine_lifes.api.event.client

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.Minecraft

private typealias ClientRespawn = (Minecraft) -> Unit

object ClientRespawnEvents {
    val RESPAWN: Event<ClientRespawn> = EventFactory.createArrayBacked(ClientRespawn::class.java) {
        { mc -> for (i in it) i(mc) }
    }
}