package com.bleudev.nine_lifes.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.function.Consumer;
import java.util.function.Function;

public class WorldUtils {
    public static void forBlocksInBox(Box box, Function<BlockPos, Integer> func) {
        Vec3d min_pos = box.getMinPos();
        Vec3d max_pos = box.getMaxPos();

        int start_x = (int) Math.floor(min_pos.getX());
        int end_x = (int) Math.ceil(max_pos.getX());

        int start_y = (int) Math.floor(min_pos.getY());
        int end_y = (int) Math.ceil(max_pos.getY());

        int start_z = (int) Math.floor(min_pos.getZ());
        int end_z = (int) Math.ceil(max_pos.getZ());


        for (int x = start_x; x <= end_x; x++)
            for (int y = start_y; y <= end_y; y++)
                for (int z = start_z; z <= end_z; z++)
                    if (func.apply(new BlockPos(x, y, z)) < 0) return;
    }

    public static void forBlocksInBox(Box box, Consumer<BlockPos> func) {
        forBlocksInBox(box, pos -> {
            func.accept(pos);
            return 0;
        });
    }
}
