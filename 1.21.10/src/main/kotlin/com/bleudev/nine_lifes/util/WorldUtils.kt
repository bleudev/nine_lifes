package com.bleudev.nine_lifes.util

import net.minecraft.core.BlockPos
import net.minecraft.world.phys.AABB
import kotlin.math.floor

fun forBlocksInBox(box: AABB, func: (BlockPos) -> Int): Boolean {
    for (x in floor(box.minX).toInt()..floor(box.maxX).toInt())
        for (y in floor(box.minY).toInt()..floor(box.maxY).toInt())
            for (z in floor(box.minZ).toInt()..floor(box.maxZ).toInt())
                if (func(BlockPos(x, y, z)) < 0) return false
    return true
}