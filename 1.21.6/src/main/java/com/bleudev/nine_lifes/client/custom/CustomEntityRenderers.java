package com.bleudev.nine_lifes.client.custom;

import com.bleudev.nine_lifes.client.custom.entity.render.WanderingArmorStandRenderer;
import com.bleudev.nine_lifes.custom.CustomEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class CustomEntityRenderers {
    public static void initialize() {
        EntityRendererRegistry.register(CustomEntities.WANDERING_ARMOR_STAND_TYPE, WanderingArmorStandRenderer::new);
    }
}
