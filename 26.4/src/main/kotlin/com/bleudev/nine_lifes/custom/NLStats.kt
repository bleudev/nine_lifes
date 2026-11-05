package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.util.createIdentifier
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.stats.StatFormatter
import net.minecraft.stats.Stats

object NLStats {
    val USED_CHARGED: Identifier = makeCustomStat("used_charged", StatFormatter.DEFAULT)

    @Suppress("SameParameterValue")
    private fun makeCustomStat(id: String, formatter: StatFormatter): Identifier {
        val location = createIdentifier(id)
        Registry.register(BuiltInRegistries.CUSTOM_STAT, id, location)
        Stats.CUSTOM.get(location, formatter)
        return location
    }

    internal fun initialize() {}
}