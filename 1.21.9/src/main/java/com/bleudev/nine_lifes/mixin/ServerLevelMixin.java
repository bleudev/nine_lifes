package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.api.event.EntitySpawnEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {
    @Inject(method = "addFreshEntity", at = @At("RETURN"))
    private void callbackSpawnEvent(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        EntitySpawnEvents.ENTITY_SPAWN.invoker().onEntitySpawn(entity, (ServerLevel) (Object) this);
    }
}
