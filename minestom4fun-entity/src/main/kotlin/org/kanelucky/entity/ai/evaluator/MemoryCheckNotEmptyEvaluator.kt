package org.kanelucky.entity.ai.evaluator

import net.minestom.server.entity.EntityCreature
import org.kanelucky.api.java.entity.ai.memory.MemoryType
import org.kanelucky.entity.passive.PassiveMob

/**
 * Checks if a specific memory type has a value stored (is not empty)
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
class MemoryCheckNotEmptyEvaluator(private val type: MemoryType<*>) : BehaviorEvaluator {
    override fun evaluate(entity: EntityCreature): Boolean {
        if (entity !is PassiveMob) return false
        return entity.behaviorGroup.memoryStorage.get(type) != null
    }
}