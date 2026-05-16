package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.custom.world.item.AmethystStickItem
import com.bleudev.nine_lifes.util.registerItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity

object NineLifesItems {
    val AMETHYST_STICK = registerItem("amethyst_stick", ::AmethystStickItem, Item.Properties().durability(5).rarity(Rarity.EPIC))

    fun initialize() {}
}