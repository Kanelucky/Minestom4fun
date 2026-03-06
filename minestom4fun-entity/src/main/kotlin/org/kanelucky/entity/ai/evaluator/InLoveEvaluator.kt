package org.kanelucky.entity.ai.evaluator

import net.minestom.server.entity.EntityCreature
import org.kanelucky.api.java.entity.ai.memory.MemoryTypes
import org.kanelucky.entity.IntelligentEntity
import org.kanelucky.entity.passive.AnimalEntity

/**
 * Returns true if the entity is in love and ready to breed
 *
 * @author Kanelucky
 */
class InLoveEvaluator : BehaviorEvaluator {
    override fun evaluate(entity: EntityCreature): Boolean {
        if (entity !is IntelligentEntity) return false
        if (entity !is AnimalEntity) return false
        if (entity.isBaby) return false
        if (entity.breedCooldown > 0) return false
        return entity.behaviorGroup.memoryStorage.get(MemoryTypes.IS_IN_LOVE) == true
    }
}