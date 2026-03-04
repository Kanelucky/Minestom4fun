package org.kanelucky.server.entity.passive

import net.minestom.server.entity.EntityType

import org.kanelucky.server.entity.IntelligentEntity
import org.kanelucky.server.entity.ai.behavior.BehaviorImpl
import org.kanelucky.server.entity.ai.behavior.WeightedMultiBehavior
import org.kanelucky.server.entity.ai.behaviorgroup.BehaviorGroup
import org.kanelucky.server.entity.ai.controller.LookController
import org.kanelucky.server.entity.ai.controller.WalkController
import org.kanelucky.server.entity.ai.evaluator.InLoveEvaluator
import org.kanelucky.server.entity.ai.evaluator.PanicEvaluator
import org.kanelucky.server.entity.ai.evaluator.ProbabilityEvaluator
import org.kanelucky.server.entity.ai.executor.EntityBreedingExecutor
import org.kanelucky.server.entity.ai.executor.FlatRandomRoamExecutor
import org.kanelucky.server.entity.ai.executor.FollowEntityExecutor
import org.kanelucky.server.entity.ai.executor.IdleExecutor
import org.kanelucky.server.entity.ai.executor.LookAtEntityExecutor
import org.kanelucky.server.entity.ai.executor.PanicExecutor
import org.kanelucky.server.entity.ai.memory.MemoryTypes
import org.kanelucky.server.entity.ai.sensor.NearestFeedingPlayerSensor
import org.kanelucky.server.entity.ai.sensor.NearestPlayerSensor
import kotlin.random.Random

/**
 * @author Kanelucky
 */
abstract class PassiveMob(entityType: EntityType) : AnimalEntity(entityType) {

    protected fun BehaviorGroup.Builder.addBaseBehaviors(): BehaviorGroup.Builder = this
        .sensor(NearestPlayerSensor())
        .sensor(NearestFeedingPlayerSensor())
        .behavior(
            BehaviorImpl.builder()
                .executor(PanicExecutor())
                .evaluator(PanicEvaluator())
                .priority(4)
                .period(1)
                .build()
        )
        .behavior(
            BehaviorImpl.builder()
                .executor(EntityBreedingExecutor())
                .evaluator(InLoveEvaluator())
                .priority(3)
                .period(1)
                .build()
        )
        .behavior(
            BehaviorImpl.builder()
                .executor(FollowEntityExecutor(
                    entityMemory = MemoryTypes.NEAREST_FEEDING_PLAYER,
                    speed = 0.125,
                    maxRangeSq = 256.0,
                    minRangeSq = 2.0
                ))
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
                            entity.behaviorGroup.memoryStorage.get(MemoryTypes.NEAREST_PLAYER) != null &&
                            Random.nextInt(100) < 5
                }
                .priority(2)
                .period(10)
                .build()
        )
        .behavior(
            WeightedMultiBehavior(
                behaviors = setOf(
                    BehaviorImpl.builder()
                        .executor(IdleExecutor(minTicks = 20, maxTicks = 100))
                        .evaluator { true }
                        .priority(1)
                        .weight(1)
                        .period(1)
                        .build(),
                    BehaviorImpl.builder()
                        .executor(FlatRandomRoamExecutor(
                            speed = 0.1,
                            maxRoamRange = 10,
                            frequency = 20,
                            avoidWater = true
                        ))
                        .evaluator { true }
                        .priority(1)
                        .weight(2)
                        .period(1)
                        .build()
                ),
                priority = 1,
                period = 40
            )
        )
        .controller(WalkController())
        .controller(LookController())

    override val behaviorGroup = BehaviorGroup.builder()
        .addBaseBehaviors()
        .build()
        .also { it.setEntity(this) }
}