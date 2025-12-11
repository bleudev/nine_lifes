package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.custom.entity.WanderingArmorStand
import com.bleudev.nine_lifes.util.registerEntity
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory

object NineLifesEntities {
    val WANDERING_ARMOR_STAND = registerEntity(
        "wandering_armor_stand",
        EntityType.Builder.of(::WanderingArmorStand, MobCategory.MISC).fireImmune().sized(.6f, 1.8f)
    )

    fun initialize() {
        FabricDefaultAttributeRegistry.register(WANDERING_ARMOR_STAND, WanderingArmorStand.createAttributes())
    }
}