package org.kanelucky.server.entity.ai.executor

import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.EntityCreature
import net.minestom.server.entity.Player
import org.kanelucky.server.entity.IntelligentEntity
import org.kanelucky.server.entity.ai.memory.MemoryType

/**
 * Looks at an entity whose runtime ID is stored in memory for a duration
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
class LookAtEntityExecutor(
    private val entityMemory: MemoryType<Player>,
    private val duration: Int = 60
) : BehaviorExecutor {

    private var tickCounter = 0

    override fun onStart(entity: EntityCreature) {
        tickCounter = 0
    }

    override fun execute(entity: EntityCreature): Boolean {
        if (entity !is IntelligentEntity) return false
        tickCounter++
        if (tickCounter > duration) return false

        val target = entity.behaviorGroup.memoryStorage.get(entityMemory) ?: return false

        val pos = target.position
        EntityControlHelper.setLookTarget(entity,
            Pos(pos.x(), pos.y() + target.eyeHeight, pos.z())
        )
        return true
    }

    override fun onStop(entity: EntityCreature) {
        if (entity !is IntelligentEntity) return
        EntityControlHelper.removeLookTarget(entity)
    }

    override fun onInterrupt(entity: EntityCreature) = onStop(entity)
}