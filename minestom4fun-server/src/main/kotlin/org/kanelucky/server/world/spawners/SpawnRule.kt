package org.kanelucky.server.world.spawners

import net.minestom.server.coordinate.Point
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block

/**
 * Defines natural spawn conditions for an entity type
 *
 * @author Kanelucky
 */
interface SpawnRule {
    val weight: Int
    val minGroupSize: Int
    val maxGroupSize: Int
    fun canSpawn(instance: Instance, pos: Point): Boolean
    fun createEntity(): net.minestom.server.entity.Entity

    fun isGrass(instance: Instance, pos: Point): Boolean {
        val chunk = instance.getChunkAt(pos) ?: return false
        if (!chunk.isLoaded) return false
        val ground = instance.getBlock(pos.blockX(), pos.blockY() - 1, pos.blockZ())
        val above = instance.getBlock(pos.blockX(), pos.blockY(), pos.blockZ())
        return ground == Block.GRASS_BLOCK && !above.isSolid
    }
}