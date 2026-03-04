package org.kanelucky.server.entity

import net.minestom.server.entity.EntityCreature
import net.minestom.server.entity.EntityType

import org.kanelucky.server.entity.ai.behaviorgroup.BehaviorGroup

abstract class IntelligentEntity(entityType: EntityType) : EntityCreature(entityType) {

    abstract val behaviorGroup: BehaviorGroup

    override fun update(time: Long) {
        super.update(time)
        behaviorGroup.tick()
    }
}