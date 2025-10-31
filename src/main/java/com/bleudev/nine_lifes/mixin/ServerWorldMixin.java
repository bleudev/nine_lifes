package com.bleudev.nine_lifes.mixin;

import com.bleudev.nine_lifes.custom.event.EntitySpawnEvents;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Inject(method = "spawnEntity", at = @At("RETURN"))
    private void callbackSpawnEvent(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        EntitySpawnEvents.ENTITY_SPAWN.invoker().onEntitySpawn(entity, (ServerWorld) (Object) this);
    }
}
