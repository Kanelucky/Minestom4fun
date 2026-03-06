package org.kanelucky.entity.ai.executor

import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.EntityCreature
import net.minestom.server.instance.block.Block
import org.kanelucky.api.java.entity.ai.memory.MemoryTypes

import org.kanelucky.entity.IntelligentEntity

import kotlin.random.Random

/**
 * Makes the entity run to a random position when damaged.
 *
 * @author Kanelucky
 */
class PanicExecutor(
    private val speed: Double = 0.15,
    private val range: Int = 8
) : BehaviorExecutor {

    override fun onStart(entity: EntityCreature) {
        entity.getAttribute(net.minestom.server.entity.attribute.Attribute.MOVEMENT_SPEED).baseValue = speed
        findPanicTarget(entity)
    }

    override fun execute(entity: EntityCreature): Boolean {
        if (entity !is IntelligentEntity) return false

        val panicTicks = entity.behaviorGroup.memoryStorage.get(MemoryTypes.PANIC_TICKS) ?: return false
        if (panicTicks <= 0) return false

        entity.behaviorGroup.memoryStorage.set(MemoryTypes.PANIC_TICKS, panicTicks - 1)

        // Re-pick target if reached
        val target = entity.behaviorGroup.memoryStorage.get(MemoryTypes.MOVE_TARGET)
        if (target != null) {
            val dx = target.x() - entity.position.x()
            val dz = target.z() - entity.position.z()
            if (dx * dx + dz * dz < 1.0) {
                findPanicTarget(entity)
            }
        } else {
            findPanicTarget(entity)
        }

        return true
    }

    override fun onStop(entity: EntityCreature) {
        entity.getAttribute(net.minestom.server.entity.attribute.Attribute.MOVEMENT_SPEED).baseValue = 0.23
        if (entity is IntelligentEntity) {
            EntityControlHelper.removeRouteTarget(entity)
            entity.behaviorGroup.memoryStorage.clear(MemoryTypes.PANIC_TICKS)
        }
    }

    override fun onInterrupt(entity: EntityCreature) = onStop(entity)

    private fun findPanicTarget(entity: EntityCreature) {
        if (entity !is IntelligentEntity) return
        val instance = entity.instance ?: return

        repeat(10) {
            val angle = Random.nextDouble() * 2 * Math.PI
            val dist = Random.nextInt(range / 2, range + 1).toDouble()
            val targetX = entity.position.x() + dist * Math.cos(angle)
            val targetZ = entity.position.z() + dist * Math.sin(angle)
            val targetY = entity.position.y()

            val chunk = instance.getChunkAt(targetX, targetZ)
            if (chunk == null || !chunk.isLoaded) return@repeat

            val block = instance.getBlock(
                Math.floor(targetX).toInt(),
                Math.floor(targetY).toInt() - 1,
                Math.floor(targetZ).toInt()
            )
            if (block == Block.WATER || block == Block.LAVA) return@repeat

            EntityControlHelper.setRouteTarget(entity, Pos(targetX, targetY, targetZ))
            return
        }
    }
}