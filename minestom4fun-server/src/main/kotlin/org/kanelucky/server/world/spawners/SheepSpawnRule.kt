package org.kanelucky.server.world.spawners

import net.minestom.server.coordinate.Point
import net.minestom.server.instance.Instance
import org.kanelucky.server.entity.passive.EntitySheep

/**
 * @author Kanelucky
 */
object SheepSpawnRule : SpawnRule {
    override val weight = 12
    override val minGroupSize = 2
    override val maxGroupSize = 3
    override fun canSpawn(instance: Instance, pos: Point) = isGrass(instance, pos)
    override fun createEntity() = EntitySheep()
}