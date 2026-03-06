package org.kanelucky.entity.ai.sensor

import net.minestom.server.entity.EntityCreature
import net.minestom.server.entity.Player
import org.kanelucky.api.java.entity.ai.memory.MemoryTypes
import org.kanelucky.entity.IntelligentEntity
import org.kanelucky.entity.passive.AnimalEntity

/**
 * Scans for the nearest player holding a breeding item.
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
class NearestFeedingPlayerSensor(
    private val range: Double = 8.0,
    override val period: Int = 20
) : Sensor {

    override fun sense(entity: EntityCreature) {
        if (entity !is IntelligentEntity) return
        if (entity !is AnimalEntity) return

        val rangeSq = range * range

        val nearest = entity.instance?.entities
            ?.filterIsInstance<Player>()
            ?.filter { player ->
                val distSq = player.position.distanceSquared(entity.position)
                if (distSq > rangeSq) return@filter false
                val item = player.itemInMainHand
                entity.isBreedingItem(item)
            }
            ?.minByOrNull { it.position.distanceSquared(entity.position) }

        entity.behaviorGroup.memoryStorage.set(MemoryTypes.NEAREST_FEEDING_PLAYER, nearest)
    }
}