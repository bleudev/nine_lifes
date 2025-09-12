package com.bleudev.nine_lifes.client.custom.entity.render;

import com.bleudev.nine_lifes.custom.entity.BlueEyesEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.util.Identifier;

import static com.bleudev.nine_lifes.Nine_lifes.MOD_ID;

public class BlueEyesRenderer extends LivingEntityRenderer<BlueEyesEntity, PlayerEntityRenderState, PlayerEntityModel> {
    public static final Identifier TEXTURE = Identifier.of(MOD_ID, "textures/entity/blue_eyes.png");

    public BlueEyesRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new PlayerEntityModel(ctx.getPart(EntityModelLayers.PLAYER), false), 0f);

        this.addFeature(new EyesFeatureRenderer<>(this) {
            @Override
            public RenderLayer getEyesTexture() {
                return RenderLayer.getEyes(TEXTURE);
            }
        });
    }

    @Override
    public PlayerEntityRenderState createRenderState() {
        return new PlayerEntityRenderState();
    }

    @Override
    public Identifier getTexture(PlayerEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    protected boolean hasLabel(BlueEyesEntity livingEntity, double d) {
        return false;
    }
}

