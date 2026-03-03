package org.kanelucky.server.entity.ai.executor

import net.minestom.server.entity.EntityCreature

import org.kanelucky.server.entity.IntelligentEntity
import org.kanelucky.server.entity.ai.memory.MemoryTypes

import kotlin.random.Random

/**
 * @author Kanelucky
 */
class WanderExecutor(private val radius: Double = 10.0) : BehaviorExecutor {

    override fun execute(entity: EntityCreature): Boolean {
        if (entity !is IntelligentEntity) return false
        return entity.behaviorGroup.memoryStorage.get(MemoryTypes.MOVE_TARGET) != null
    }

    override fun onStart(entity: EntityCreature) {
        if (entity !is IntelligentEntity) return
        val pos = entity.position
        val target = pos.add(
            (Random.nextDouble() - 0.5) * radius * 2,
            0.0,
            (Random.nextDouble() - 0.5) * radius * 2
        )
        entity.behaviorGroup.memoryStorage.set(MemoryTypes.MOVE_TARGET, target)
    }

    override fun onStop(entity: EntityCreature) {
        if (entity !is IntelligentEntity) return
        entity.behaviorGroup.memoryStorage.clear(MemoryTypes.MOVE_TARGET)
    }
}