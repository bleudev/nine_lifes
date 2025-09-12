package com.bleudev.nine_lifes.custom;

import com.bleudev.nine_lifes.custom.entity.WanderingArmorStand;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

import static com.bleudev.nine_lifes.util.RegistryUtils.registerEntity;

public class CustomEntities {
    public static final EntityType<WanderingArmorStand> WANDERING_ARMOR_STAND_TYPE = registerEntity(
        "wandering_armor_stand", EntityType.Builder.create(WanderingArmorStand::new, SpawnGroup.MISC).makeFireImmune().dimensions(0.6f, 1.8f)
    );

    public static void initialize() {
        FabricDefaultAttributeRegistry.register(WANDERING_ARMOR_STAND_TYPE, WanderingArmorStand.createLivingAttributes());
    }
}
