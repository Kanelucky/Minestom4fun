package org.kanelucky.server.entity.passive

import net.minestom.server.entity.EntityType
import net.minestom.server.item.Material
import org.kanelucky.server.entity.IntelligentEntity

import org.kanelucky.server.entity.ai.behavior.BehaviorImpl
import org.kanelucky.server.entity.ai.behaviorgroup.BehaviorGroup
import org.kanelucky.server.entity.ai.controller.LookController
import org.kanelucky.server.entity.ai.controller.WalkController
import org.kanelucky.server.entity.ai.evaluator.ProbabilityEvaluator
import org.kanelucky.server.entity.ai.executor.FlatRandomRoamExecutor
import org.kanelucky.server.entity.ai.executor.FollowEntityExecutor
import org.kanelucky.server.entity.ai.executor.LookAtEntityExecutor
import org.kanelucky.server.entity.ai.executor.SheepEatGrassExecutor
import org.kanelucky.server.entity.ai.memory.MemoryTypes
import org.kanelucky.server.entity.ai.sensor.NearestFeedingPlayerSensor
import org.kanelucky.server.entity.ai.sensor.NearestPlayerSensor

/**
 * @author Kanelucky
 */
class EntitySheep : PassiveMob(EntityType.SHEEP) {

    override val breedingItems = setOf(Material.WHEAT)
    var isSheared: Boolean = false

    override val behaviorGroup = BehaviorGroup.builder()
        .sensor(NearestPlayerSensor())
        .sensor(NearestFeedingPlayerSensor())
        .behavior(
            BehaviorImpl.builder()
                .executor(
                    FollowEntityExecutor(
                        entityMemory = MemoryTypes.NEAREST_FEEDING_PLAYER,
                        speed = 0.15,
                        maxRangeSq = 256.0,
                        minRangeSq = 2.0
                    )
                )
                .evaluator { entity ->
                    entity is IntelligentEntity &&
                            entity.behaviorGroup.memoryStorage.get(MemoryTypes.NEAREST_FEEDING_PLAYER) != null
                }
                .priority(3)
                .period(1)
                .build()
        )
        .behavior(
            BehaviorImpl.builder()
                .executor(LookAtEntityExecutor(MemoryTypes.NEAREST_PLAYER, 60))
                .evaluator { entity ->
                    entity is IntelligentEntity &&
                            entity.behaviorGroup.memoryStorage.get(MemoryTypes.NEAREST_PLAYER) != null
                }
                .priority(2)
                .period(1)
                .build()
        )
        .behavior(
            BehaviorImpl.builder()
                .executor(SheepEatGrassExecutor())
                .evaluator(ProbabilityEvaluator(1, 200))
                .priority(2)
                .period(40)
                .build()
        )
        .behavior(
            BehaviorImpl.builder()
                .executor(FlatRandomRoamExecutor(
                    speed = 0.23,
                    maxRoamRange = 10,
                    frequency = 20,
                    avoidWater = true
                ))
                .evaluator(ProbabilityEvaluator(1, 1))
                .priority(1)
                .period(1)
                .build()
        )
        .controller(WalkController())
        .controller(LookController())
        .build()
        .also { it.setEntity(this) }
}