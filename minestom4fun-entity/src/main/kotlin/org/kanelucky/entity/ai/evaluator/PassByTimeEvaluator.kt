package org.kanelucky.entity.ai.evaluator

import net.minestom.server.entity.EntityCreature
import org.kanelucky.api.java.entity.ai.memory.MemoryType
import org.kanelucky.entity.passive.PassiveMob

/**
 * Checks if the time elapsed since a timed value is within
 * the range {@code [min, max]}
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
class PassByTimeEvaluator(
    private val timedMemory: MemoryType<Long>,
    private val min: Long,
    private val max: Long
) : BehaviorEvaluator {
    override fun evaluate(entity: EntityCreature): Boolean {
        if (entity !is PassiveMob) return false
        val lastTime = entity.behaviorGroup.memoryStorage.get(timedMemory) ?: return false
        val elapsed = entity.aliveTicks - lastTime
        return elapsed in min..max
    }
}