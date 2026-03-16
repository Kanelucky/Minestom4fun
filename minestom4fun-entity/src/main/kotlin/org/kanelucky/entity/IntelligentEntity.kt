package org.kanelucky.entity

import net.minestom.server.entity.EntityCreature
import net.minestom.server.entity.EntityType
import org.kanelucky.entity.ai.behaviorgroup.BehaviorGroup

/**
 * @author Kanelucky
 */
abstract class IntelligentEntity(entityType: EntityType) : EntityCreature(entityType) {

    abstract val behaviorGroup: BehaviorGroup

    override fun update(time: Long) {
        super.update(time)
        behaviorGroup.tick()
    }
}