package com.bleudev.nine_lifes.mixin.client;

import com.bleudev.nine_lifes.interfaces.mixin.ILivingEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Environment(EnvType.CLIENT)
@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements ILivingEntityRenderState {
    @Unique
    @Mutable
    private UUID entityId;

    @Override
    public UUID entityId() {
        return this.entityId;
    }

    @Override
    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }
}
