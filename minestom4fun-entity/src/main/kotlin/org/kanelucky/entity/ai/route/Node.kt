package org.kanelucky.entity.ai.route

import net.minestom.server.coordinate.Vec

/**
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
class Node(val x: Double, val y: Double, val z: Double) : Comparable<Node> {
    var g: Double = 0.0
    var h: Double = 0.0
    var parent: Node? = null

    val f get() = g + h
    val vec get() = Vec(x, y, z)

    override fun compareTo(other: Node) = f.compareTo(other.f)
    override fun equals(other: Any?) = other is Node &&
            Math.floor(x).toInt() == Math.floor(other.x).toInt() &&
            Math.floor(z).toInt() == Math.floor(other.z).toInt()
    override fun hashCode() = 31 * Math.floor(x).toInt() + Math.floor(z).toInt()
}