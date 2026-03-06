package org.kanelucky.entity.ai.executor

import net.minestom.server.entity.EntityCreature
import kotlin.random.Random

/**
 * @author Kanelucky
 */
class LookAroundExecutor : BehaviorExecutor {

    private var ticksLeft = 0

    override fun execute(entity: EntityCreature): Boolean {
        ticksLeft--
        return ticksLeft > 0
    }

    override fun onStart(entity: EntityCreature) {
        ticksLeft = Random.nextInt(20, 60)
        val yaw = Random.nextFloat() * 360f - 180f
        entity.teleport(entity.position.withYaw(yaw))
    }
}