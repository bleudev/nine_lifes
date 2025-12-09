package com.bleudev.nine_lifes.custom

import com.bleudev.nine_lifes.custom.entity.WanderingArmorStandEntity
import com.bleudev.nine_lifes.util.registerEntity
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.core.RegistryAccess
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory

object NineLifesEntities {
    lateinit var WANDERING_ARMOR_STAND_TYPE: EntityType<WanderingArmorStandEntity>

    fun initialize(registryAccess: RegistryAccess) {
        WANDERING_ARMOR_STAND_TYPE = registerEntity(
            registryAccess,
            "wandering_armor_stand",
            EntityType.Builder.of(::WanderingArmorStandEntity, MobCategory.MISC).fireImmune().sized(.6f, 1.8f)
        )
        FabricDefaultAttributeRegistry.register(WANDERING_ARMOR_STAND_TYPE, WanderingArmorStandEntity.createLivingAttributes())
    }
}