package org.kanelucky.entity.ai.executor

import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.EntityCreature
import net.minestom.server.entity.attribute.Attribute
import net.minestom.server.instance.block.Block
import org.kanelucky.api.java.entity.ai.memory.MemoryTypes

import org.kanelucky.entity.IntelligentEntity

import kotlin.random.Random

/**
 * Random wandering executor. Picks random XZ targets within range
 * and sets route/look targets
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
class FlatRandomRoamExecutor(
    private val speed: Double = 0.2,
    private val maxRoamRange: Int = 10,
    private val frequency: Int = 20,
    private val calNextTargetImmediately: Boolean = true,
    private val runningTime: Int = 100,
    private val avoidWater: Boolean = true,
    private val maxRetryTime: Int = 10
) : BehaviorExecutor {

    private var durationTick = 0
    private var targetCalTick = 0
    private var retryCount = 0
    private var hasTarget = false

    override fun onStart(entity: EntityCreature) {
        durationTick = 0
        targetCalTick = 0
        retryCount = 0
        hasTarget = false

        entity.getAttribute(Attribute.MOVEMENT_SPEED).baseValue = speed

        findNewTarget(entity)
    }

    override fun execute(entity: EntityCreature): Boolean {
        durationTick++
        targetCalTick++

        if (entity !is IntelligentEntity) return false

        val moveTarget = entity.behaviorGroup.memoryStorage.get(MemoryTypes.MOVE_TARGET)
        if (moveTarget != null) {
            val pos = entity.position
            val dx = moveTarget.x() - pos.x()
            val dz = moveTarget.z() - pos.z()
            if (dx * dx + dz * dz < 1.0) {
                hasTarget = false
                if (!calNextTargetImmediately) {
                    EntityControlHelper.removeRouteTarget(entity)
                    EntityControlHelper.removeLookTarget(entity)
                }
            }
        }

        if (!hasTarget) {
            if (calNextTargetImmediately) {
                findNewTarget(entity)
            } else if (targetCalTick >= frequency) {
                findNewTarget(entity)
            }
        }

        return runningTime <= 0 || durationTick <= runningTime
    }

    override fun onStop(entity: EntityCreature) {
        if (entity !is IntelligentEntity) return
        entity.getAttribute(Attribute.MOVEMENT_SPEED).baseValue = 0.25
        EntityControlHelper.removeRouteTarget(entity)
        EntityControlHelper.removeLookTarget(entity)
    }

    override fun onInterrupt(entity: EntityCreature) = onStop(entity)

    private fun findNewTarget(entity: EntityCreature) {
        if (entity !is IntelligentEntity) return
        if (retryCount >= maxRetryTime) return

        val instance = entity.instance ?: return
        val pos = entity.position
        val targetX = pos.x() + Random.nextInt(-maxRoamRange, maxRoamRange + 1)
        val targetZ = pos.z() + Random.nextInt(-maxRoamRange, maxRoamRange + 1)
        val targetY = pos.y()

        val chunk = instance.getChunkAt(targetX, targetZ)
        if (chunk == null || !chunk.isLoaded) {
            retryCount++
            return
        }

        if (avoidWater) {
            val block = instance.getBlock(
                Math.floor(targetX).toInt(),
                Math.floor(targetY).toInt() - 1,
                Math.floor(targetZ).toInt()
            )
            if (block == Block.WATER) {
                retryCount++
                return
            }
        }

        val target = Pos(targetX, targetY, targetZ)
        EntityControlHelper.setRouteTarget(entity, target)
        EntityControlHelper.setLookTarget(entity, target)
        hasTarget = true
        targetCalTick = 0
        retryCount = 0
    }
}