package org.kanelucky.entity.ai.executor

import net.minestom.server.coordinate.Point
import net.minestom.server.entity.EntityCreature
import org.kanelucky.api.java.entity.ai.memory.MemoryType
import org.kanelucky.entity.IntelligentEntity

/**
 * Moves the entity toward a position stored in a memory type
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
class MoveToTargetExecutor(
    private val memoryType: MemoryType<Point>,
    private val speed: Double = 0.2,
    private val maxFollowRangeSq: Double = 256.0,
    private val minFollowRangeSq: Double = 0.0
) : BehaviorExecutor {

    private var lastTarget: Point? = null

    override fun onStart(entity: EntityCreature) {
        lastTarget = null
    }

    override fun execute(entity: EntityCreature): Boolean {
        if (entity !is IntelligentEntity) return false

        val target = entity.behaviorGroup.memoryStorage.get(memoryType) ?: return false
        val distSq = entity.position.distanceSquared(target)

        if (distSq > maxFollowRangeSq || distSq < minFollowRangeSq) return false

        if (lastTarget == null || target.distanceSquared(lastTarget!!) > 1.0) {
            EntityControlHelper.setRouteTarget(entity, target)
            EntityControlHelper.setLookTarget(entity, target)
            lastTarget = target
        }
        return true
    }

    override fun onStop(entity: EntityCreature) {
        if (entity !is IntelligentEntity) return
        EntityControlHelper.removeRouteTarget(entity)
        EntityControlHelper.removeLookTarget(entity)
        lastTarget = null
    }

    override fun onInterrupt(entity: EntityCreature) = onStop(entity)
}