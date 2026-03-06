package org.kanelucky.entity.ai.executor

import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.EntityCreature
import net.minestom.server.entity.Player
import net.minestom.server.entity.attribute.Attribute
import org.kanelucky.api.java.entity.ai.memory.MemoryType

import org.kanelucky.entity.IntelligentEntity

/**
 * Follows an entity whose runtime ID is stored in memory
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
class FollowEntityExecutor(
    private val entityMemory: MemoryType<Player>,
    private val speed: Double = 0.125,
    private val maxRangeSq: Double = 256.0,
    private val minRangeSq: Double = 4.0
) : BehaviorExecutor {

    private var lastTargetPos: Pos? = null

    override fun onStart(entity: EntityCreature) {
        lastTargetPos = null
        entity.getAttribute(Attribute.MOVEMENT_SPEED).baseValue = speed
    }

    override fun execute(entity: EntityCreature): Boolean {
        if (entity !is IntelligentEntity) return false

        val target = entity.behaviorGroup.memoryStorage.get(entityMemory) ?: return false

        val targetPos = target.position
        val distSq = entity.position.distanceSquared(targetPos)

        if (distSq > maxRangeSq) return false

        if (distSq > minRangeSq) {
            val pos = Pos(targetPos.x(), targetPos.y(), targetPos.z())
            if (lastTargetPos == null || lastTargetPos!!.distanceSquared(pos) > 1.0) {
                EntityControlHelper.setRouteTarget(entity, pos)
                lastTargetPos = pos
            }
        } else {
            EntityControlHelper.removeRouteTarget(entity)
        }

        EntityControlHelper.setLookTarget(entity,
            Pos(targetPos.x(), targetPos.y() + target.eyeHeight, targetPos.z())
        )
        return true
    }

    override fun onStop(entity: EntityCreature) {
        if (entity !is IntelligentEntity) return
        entity.getAttribute(Attribute.MOVEMENT_SPEED).baseValue = 0.23
        EntityControlHelper.removeRouteTarget(entity)
        EntityControlHelper.removeLookTarget(entity)
    }

    override fun onInterrupt(entity: EntityCreature) = onStop(entity)
}