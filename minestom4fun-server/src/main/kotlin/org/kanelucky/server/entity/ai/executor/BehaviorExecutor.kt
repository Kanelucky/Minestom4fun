package org.kanelucky.server.entity.ai.executor

import net.minestom.server.entity.EntityCreature

interface BehaviorExecutor {
    fun execute(entity: EntityCreature): Boolean
    fun onStart(entity: EntityCreature) {}
    fun onStop(entity: EntityCreature) {}
    fun onInterrupt(entity: EntityCreature) {}
}