package org.kanelucky.server.entity.ai.executor

import net.minestom.server.entity.EntityCreature

import org.kanelucky.server.entity.IntelligentEntity

import kotlin.random.Random

/**
 * Makes the entity stand still for a random duration.
 *
 * @author Kanelucky
 */
class IdleExecutor(
    private val minTicks: Int = 20,
    private val maxTicks: Int = 60
) : BehaviorExecutor {

    private var ticksLeft = 0

    override fun onStart(entity: EntityCreature) {
        ticksLeft = Random.nextInt(minTicks, maxTicks)
        if (entity is IntelligentEntity) {
            EntityControlHelper.removeRouteTarget(entity)
        }
    }

    override fun execute(entity: EntityCreature): Boolean {
        ticksLeft--
        return ticksLeft > 0
    }
}