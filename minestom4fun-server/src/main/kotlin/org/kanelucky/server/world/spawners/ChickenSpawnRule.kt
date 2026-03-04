package org.kanelucky.server.world.spawners

import net.minestom.server.coordinate.Point
import net.minestom.server.instance.Instance
import org.kanelucky.server.entity.passive.EntityChicken

/**
 * @author Kanelucky
 */
object ChickenSpawnRule : SpawnRule {
    override val weight = 10
    override val minGroupSize = 2
    override val maxGroupSize = 4
    override fun canSpawn(instance: Instance, pos: Point) = isGrass(instance, pos)
    override fun createEntity() = EntityChicken()
}