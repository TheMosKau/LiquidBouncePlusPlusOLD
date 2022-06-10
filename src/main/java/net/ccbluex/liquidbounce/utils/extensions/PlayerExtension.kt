/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 */
package net.ccbluex.liquidbounce.utils.extensions

import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.Vec3
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Allows to get the distance between the current entity and [entity] from the nearest corner of the bounding box
 */
fun Entity.getDistanceToEntityBox(entity: Entity): Double {
    return sqrt(squaredBoxedDistanceTo(entity))
}

fun Entity.squaredBoxedDistanceTo(entity: Entity): Double {
    val eyes = this.getPositionEyes(1F)
    val pos = getNearestPointBB(eyes, entity.entityBoundingBox)

    val xDist = pos.xCoord - eyes.xCoord
    val yDist = pos.yCoord - eyes.yCoord
    val zDist = pos.zCoord - eyes.zCoord

    return xDist * xDist + yDist * yDist + zDist * zDist
}

fun getNearestPointBB(eyes: Vec3d, box: Box): Vec3d {
    val origin = doubleArrayOf(eyes.xCoord, eyes.yCoord, eyes.zCoord)
    val destMins = doubleArrayOf(box.minX, box.minY, box.minZ)
    val destMaxs = doubleArrayOf(box.maxX, box.maxY, box.maxZ)

    // It loops through every coordinate of the double arrays and picks the nearest point
    for (i in 0..2) {
        if (origin[i] > destMaxs[i]) {
            origin[i] = destMaxs[i]
        } else if (origin[i] < destMins[i]) {
            origin[i] = destMins[i]
        }
    }

    return Vec3d(origin[0], origin[1], origin[2])
}
