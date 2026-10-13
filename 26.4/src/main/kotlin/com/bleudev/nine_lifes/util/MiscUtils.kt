package com.bleudev.nine_lifes.util

import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

fun ofDXYZ(center: Vec3, dxyz: Double): AABB = AABB.ofSize(center, dxyz, dxyz, dxyz)
fun ofDXYZ(center: Vec3, dxyz: Int): AABB = ofDXYZ(center, dxyz.toDouble())