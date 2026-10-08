package com.bleudev.nine_lifes.util

import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

typealias P<T> = (T) -> Boolean

fun <T> alwaysTrue(): (T) -> Boolean = {true}

fun <T : Entity> entityIn(box: AABB): P<T> = { box.contains(it.position()) }
fun <T : Entity> entityIn(center: Vec3, dx: Double, dy: Double, dz: Double): P<T> = entityIn(AABB.ofSize(center, dx, dy, dz))
fun <T : Entity> entityIn(center: Vec3, dx: Int, dy: Int, dz: Int): P<T> = entityIn(center, dx.toDouble(), dy.toDouble(), dz.toDouble())
fun <T : Entity> entityIn(center: Vec3, dxyz: Int): P<T> = entityIn(center, dxyz, dxyz, dxyz)

fun <T> P<T>.and(vararg other: P<T>): P<T> = {
    var p = this(it)
    for (i in other) p = p && i(it)
    p
}
fun <T> P<T>.or(vararg other: P<T>): P<T> = {
    var p = this(it)
    for (i in other) p = p || i(it)
    p
}