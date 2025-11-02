package com.bleudev.nine_lifes.client.custom.entity.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.ArmorStandEntityModel;
import net.minecraft.client.render.entity.state.ArmorStandEntityRenderState;
import net.minecraft.util.math.MathHelper;

public class WanderingArmorStandModel extends ArmorStandEntityModel {
    public WanderingArmorStandModel(ModelPart modelPart) {
        super(modelPart);
    }

    public void setAngles(ArmorStandEntityRenderState state) {
        super.setAngles(state);

        float f = state.limbSwingAnimationProgress;
        float g = state.limbSwingAmplitude;
        this.rightLeg.pitch = MathHelper.cos(f * 0.6662F) * 1.4F * g;
        this.leftLeg.pitch = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * g;
        this.rightArm.pitch = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.4F * g;
        this.leftArm.pitch = MathHelper.cos(f * 0.6662F) * 1.4F * g;
    }
}
