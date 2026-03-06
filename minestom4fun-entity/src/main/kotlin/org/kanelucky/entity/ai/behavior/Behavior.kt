package org.kanelucky.entity.ai.behavior

import net.minestom.server.entity.EntityCreature

enum class BehaviorState { ACTIVE, STOP }

interface Behavior {
    val priority: Int
    val weight: Int
    val period: Int
    var behaviorState: BehaviorState

    fun evaluate(entity: EntityCreature): Boolean
    fun execute(entity: EntityCreature): Boolean
    fun onStart(entity: EntityCreature) {}
    fun onStop(entity: EntityCreature) {}
    fun onInterrupt(entity: EntityCreature) {}
}