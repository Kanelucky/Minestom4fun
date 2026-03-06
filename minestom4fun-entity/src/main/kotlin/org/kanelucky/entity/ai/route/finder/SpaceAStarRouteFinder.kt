package org.kanelucky.entity.ai.route.finder

import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.EntityCreature
import net.minestom.server.instance.Instance
import org.kanelucky.entity.ai.route.Node
import org.kanelucky.entity.ai.route.posevaluator.SpacePosEvaluator

import kotlin.math.*

/**
 * 3D A* pathfinder for flying/swimming entities.
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
class SpaceAStarRouteFinder(
    private val spacePosEvaluator: SpacePosEvaluator,
    maxExpandedNodes: Int = 100,
    maxFallDistance: Int = 3
) : FlatAStarRouteFinder(null, maxExpandedNodes, maxFallDistance) {

    companion object {
        val SQRT3_MINUS_SQRT2 = sqrt(3.0) - sqrt(2.0)
        val SPACE_NEIGHBORS = arrayOf(
            // Same level
            intArrayOf(1,0,0), intArrayOf(-1,0,0), intArrayOf(0,0,1), intArrayOf(0,0,-1),
            intArrayOf(1,0,1), intArrayOf(1,0,-1), intArrayOf(-1,0,1), intArrayOf(-1,0,-1),
            // Up
            intArrayOf(0,1,0),
            intArrayOf(1,1,0), intArrayOf(-1,1,0), intArrayOf(0,1,1), intArrayOf(0,1,-1),
            intArrayOf(1,1,1), intArrayOf(1,1,-1), intArrayOf(-1,1,1), intArrayOf(-1,1,-1),
            // Down
            intArrayOf(0,-1,0),
            intArrayOf(1,-1,0), intArrayOf(-1,-1,0), intArrayOf(0,-1,1), intArrayOf(0,-1,-1),
            intArrayOf(1,-1,1), intArrayOf(1,-1,-1), intArrayOf(-1,-1,1), intArrayOf(-1,-1,-1)
        )
    }

    override fun getNeighbors(current: Node, instance: Instance, entity: EntityCreature): List<Node> {
        val neighbors = mutableListOf<Node>()
        val cx = Math.floor(current.x).toInt()
        val cy = Math.floor(current.y).toInt()
        val cz = Math.floor(current.z).toInt()

        for (offset in SPACE_NEIGHBORS) {
            val nx = cx + offset[0]; val ny = cy + offset[1]; val nz = cz + offset[2]
            val block = instance.getBlock(nx, ny, nz)
            if (!block.isSolid && spacePosEvaluator.evaluate(entity, Vec(nx + 0.5, ny + 0.5, nz + 0.5))) {
                neighbors.add(Node(nx + 0.5, ny + 0.5, nz + 0.5))
            }
        }
        return neighbors
    }

    override fun isCloseEnough(a: Node, b: Node) = a.vec.distanceSquared(b.vec) < 1.5

    override fun heuristic(a: Node, b: Node): Double {
        val dx = abs(a.x - b.x); val dy = abs(a.y - b.y); val dz = abs(a.z - b.z)
        val max = maxOf(dx, dy, dz); val min = minOf(dx, dy, dz)
        val mid = dx + dy + dz - max - min
        return SQRT3_MINUS_SQRT2 * min + SQRT2_MINUS_1 * mid + max
    }
}