package com.bleudev.nine_lifes.compat;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class VersionCompat {
    public static Vec3d getPosCompat(@NotNull Entity entity) {
        return entity.getEntityPos();
    }
    public static World getWorldCompat(@NotNull Entity entity) {
        return entity.getEntityWorld();
    }
}
