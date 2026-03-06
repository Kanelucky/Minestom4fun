package org.kanelucky.entity.passive

import net.minestom.server.entity.EntityType
import net.minestom.server.item.Material

import net.minestom.server.sound.SoundEvent
import org.kanelucky.entity.ai.behavior.BehaviorImpl
import org.kanelucky.entity.ai.behaviorgroup.BehaviorGroup
import org.kanelucky.entity.ai.evaluator.ProbabilityEvaluator
import org.kanelucky.entity.ai.executor.SheepEatGrassExecutor

/**
 * @author Kanelucky
 */
class EntitySheep : PassiveMob(EntityType.SHEEP) {

    override val breedingItems = setOf(Material.WHEAT)
    var isSheared: Boolean = false

    override fun createOffspring(): AnimalEntity = EntitySheep()

    override fun getHurtSound() = SoundEvent.ENTITY_SHEEP_HURT

    override val behaviorGroup = BehaviorGroup.builder()
        .addBaseBehaviors()
        .behavior(
            BehaviorImpl.builder()
                .executor(SheepEatGrassExecutor())
                .evaluator(ProbabilityEvaluator(1, 200))
                .priority(2)
                .period(40)
                .build()
        )
        .build()
        .also { it.setEntity(this) }
}