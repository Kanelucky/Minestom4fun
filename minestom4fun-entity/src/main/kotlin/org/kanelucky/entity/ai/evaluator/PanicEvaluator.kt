package org.kanelucky.entity.ai.evaluator

import net.minestom.server.entity.EntityCreature
import org.kanelucky.api.java.entity.ai.memory.MemoryTypes
import org.kanelucky.entity.IntelligentEntity

/**
 * Returns true if the entity has panic ticks remaining
 *
 * @author Kanelucky
 */
class PanicEvaluator : BehaviorEvaluator {
    override fun evaluate(entity: EntityCreature): Boolean {
        if (entity !is IntelligentEntity) return false
        return (entity.behaviorGroup.memoryStorage.get(MemoryTypes.PANIC_TICKS) ?: 0) > 0
    }
}