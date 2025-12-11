package com.bleudev.nine_lifes.client.custom.entity.render;

import com.bleudev.nine_lifes.client.custom.entity.model.WanderingArmorStandModel;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
import net.minecraft.util.Identifier;

public class WanderingArmorStandRenderer extends LivingEntityRenderer<WanderingArmorStandEntity, ArmorStandEntityRenderState, WanderingArmorStandModel> {
    public WanderingArmorStandRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new WanderingArmorStandModel(ctx.getPart(EntityModelLayers.ARMOR_STAND)), 0f);
    }

    @Override
    public Identifier getTexture(ArmorStandEntityRenderState state) {
        return ArmorStandEntityRenderer.TEXTURE;
    }

    @Override
    public ArmorStandEntityRenderState createRenderState() {
        return new ArmorStandEntityRenderState();
    }

    @Override
    protected boolean hasLabel(WanderingArmorStandEntity livingEntity, double d) {
        if (livingEntity.hasCustomName())
            return super.hasLabel(livingEntity, d);
        return false;
    }

    @Override
    public void updateRenderState(WanderingArmorStandEntity entity, ArmorStandEntityRenderState state, float f) {
        super.updateRenderState(entity, state, f);
        state.showArms = true;
        if (entity.isAlive()) {
            state.limbSwingAnimationProgress = entity.limbAnimator.getAnimationProgress(f);
            state.limbSwingAmplitude = entity.limbAnimator.getAmplitude(f);
        } else {
            state.limbSwingAnimationProgress = 0f;
            state.limbSwingAmplitude = 0f;
        }
    }
}
