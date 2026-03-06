package org.kanelucky.entity.ai.behaviorgroup

import net.minestom.server.entity.EntityCreature

import org.kanelucky.api.java.entity.ai.memory.MemoryStorage

import org.kanelucky.entity.ai.behavior.Behavior
import org.kanelucky.entity.ai.behavior.BehaviorState
import org.kanelucky.entity.ai.controller.Controller
import org.kanelucky.entity.ai.memory.MemoryStorageImpl
import org.kanelucky.entity.ai.sensor.Sensor

/**
 * Full AI orchestrator with priority-based behavior selection, period timers,
 * and route management
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
class BehaviorGroup(
    val coreBehaviors: Set<Behavior> = emptySet(),
    val behaviors: Set<Behavior> = emptySet(),
    val sensors: Set<Sensor> = emptySet(),
    val controllers: Set<Controller> = emptySet(),
    val memoryStorage: MemoryStorage = MemoryStorageImpl()
) {
    private var entity: EntityCreature? = null
    private val sensorCounters = mutableMapOf<Sensor, Int>()
    private val coreBehaviorCounters = mutableMapOf<Behavior, Int>()
    private val behaviorCounters = mutableMapOf<Behavior, Int>()
    private val runningCoreBehaviors = linkedSetOf<Behavior>()
    private val runningBehaviors = linkedSetOf<Behavior>()

    fun setEntity(entity: EntityCreature) {
        this.entity = entity
        sensors.forEach { sensorCounters[it] = 0 }
        coreBehaviors.forEach { coreBehaviorCounters[it] = 0 }
        behaviors.forEach { behaviorCounters[it] = 0 }
    }

    fun tick() {
        val entity = entity ?: run {
            return
        }
        collectSensorData(entity)
        evaluateCoreBehaviors(entity)
        evaluateBehaviors(entity)
        tickRunning(entity, runningCoreBehaviors)
        tickRunning(entity, runningBehaviors)
        controllers.forEach { it.control(entity) }
    }

    private fun collectSensorData(entity: EntityCreature) {
        for (sensor in sensors) {
            val counter = (sensorCounters[sensor] ?: 0) + 1
            if (counter >= sensor.period) {
                sensor.sense(entity)
                sensorCounters[sensor] = 0
            } else {
                sensorCounters[sensor] = counter
            }
        }
    }

    private fun evaluateCoreBehaviors(entity: EntityCreature) {
        for (behavior in coreBehaviors) {
            if (behavior in runningCoreBehaviors) continue
            val counter = (coreBehaviorCounters[behavior] ?: 0) + 1
            coreBehaviorCounters[behavior] = if (counter < behavior.period) counter else 0
            if (counter < behavior.period) continue
            if (behavior.evaluate(entity)) {
                behavior.onStart(entity)
                behavior.behaviorState = BehaviorState.ACTIVE
                runningCoreBehaviors.add(behavior)
            }
        }
    }

    private fun evaluateBehaviors(entity: EntityCreature) {
        val candidates = mutableSetOf<Behavior>()
        var highestPriority = Int.MIN_VALUE

        for (behavior in behaviors) {
            if (behavior in runningBehaviors) continue
            val counter = (behaviorCounters[behavior] ?: 0) + 1
            behaviorCounters[behavior] = if (counter < behavior.period) counter else 0

            if (counter < behavior.period) continue

            val result = behavior.evaluate(entity)

            if (!result) continue
            when {
                behavior.priority > highestPriority -> {
                    candidates.clear()
                    highestPriority = behavior.priority
                    candidates.add(behavior)
                }
                behavior.priority == highestPriority -> candidates.add(behavior)
            }
        }

        if (candidates.isEmpty()) return

        val runningPriority = runningBehaviors.firstOrNull()?.priority ?: Int.MIN_VALUE
        when {
            highestPriority > runningPriority -> {
                interruptRunningBehaviors(entity)
                startBehaviors(entity, candidates)
            }
            highestPriority == runningPriority -> startBehaviors(entity, candidates)
        }
    }

    private fun interruptRunningBehaviors(entity: EntityCreature) {
        runningBehaviors.forEach { it.onInterrupt(entity); it.behaviorState = BehaviorState.STOP }
        runningBehaviors.clear()
    }

    private fun startBehaviors(entity: EntityCreature, toStart: Set<Behavior>) {
        toStart.forEach {
            it.onStart(entity)
            it.behaviorState = BehaviorState.ACTIVE
            runningBehaviors.add(it)
        }
    }

    private fun tickRunning(entity: EntityCreature, running: MutableSet<Behavior>) {
        val it = running.iterator()
        while (it.hasNext()) {
            val behavior = it.next()
            if (!behavior.execute(entity)) {
                behavior.onStop(entity)
                behavior.behaviorState = BehaviorState.STOP
                it.remove()
            }
        }
    }

    companion object {
        fun builder() = Builder()
    }

    class Builder {
        private val coreBehaviors = linkedSetOf<Behavior>()
        private val behaviors = linkedSetOf<Behavior>()
        private val sensors = linkedSetOf<Sensor>()
        private val controllers = linkedSetOf<Controller>()
        private var memoryStorage: MemoryStorage = MemoryStorageImpl()

        fun coreBehavior(b: Behavior) = apply { coreBehaviors.add(b) }
        fun behavior(b: Behavior) = apply { behaviors.add(b) }
        fun sensor(s: Sensor) = apply { sensors.add(s) }
        fun controller(c: Controller) = apply { controllers.add(c) }
        fun memoryStorage(m: MemoryStorage) = apply { memoryStorage = m }
        fun build() = BehaviorGroup(coreBehaviors, behaviors, sensors, controllers, memoryStorage)
    }
}