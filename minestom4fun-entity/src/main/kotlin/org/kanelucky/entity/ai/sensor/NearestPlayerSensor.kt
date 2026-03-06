package org.kanelucky.entity.ai.sensor

import net.minestom.server.entity.EntityCreature
import net.minestom.server.entity.Player
import org.kanelucky.api.java.entity.ai.memory.MemoryTypes
import org.kanelucky.entity.IntelligentEntity

/**
 * Scans for the nearest player within range.
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
class NearestPlayerSensor(
    private val range: Double = 16.0,
    private val minRange: Double = 0.0,
    override val period: Int = 20
) : Sensor {

    override fun sense(entity: EntityCreature) {
        if (entity !is IntelligentEntity) return
        val rangeSq = range * range
        val minRangeSq = minRange * minRange

        val nearest = entity.instance?.entities
            ?.filterIsInstance<Player>()
            ?.filter {
                val distSq = it.position.distanceSquared(entity.position)
                distSq in minRangeSq..rangeSq
            }
            ?.minByOrNull { it.position.distanceSquared(entity.position) }

        entity.behaviorGroup.memoryStorage.set(MemoryTypes.NEAREST_PLAYER, nearest)
    }
}