package org.kanelucky.entity.ai.behavior

import net.minestom.server.entity.EntityCreature

import org.kanelucky.entity.ai.evaluator.BehaviorEvaluator
import org.kanelucky.entity.ai.executor.BehaviorExecutor

/**
 * A simple behavior that delegates evaluation and execution to separate
 * {@link BehaviorEvaluator} and {@link BehaviorExecutor} instances
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
class BehaviorImpl(
    private val executor: BehaviorExecutor,
    private val evaluator: BehaviorEvaluator,
    override val priority: Int,
    override val weight: Int = 1,
    override val period: Int = 1
) : AbstractBehavior() {

    override fun evaluate(entity: EntityCreature) = evaluator.evaluate(entity)
    override fun execute(entity: EntityCreature) = executor.execute(entity)
    override fun onStart(entity: EntityCreature) = executor.onStart(entity)
    override fun onStop(entity: EntityCreature) = executor.onStop(entity)
    override fun onInterrupt(entity: EntityCreature) = executor.onInterrupt(entity)

    companion object {
        fun builder() = Builder()
    }

    class Builder {
        private lateinit var executor: BehaviorExecutor
        private lateinit var evaluator: BehaviorEvaluator
        private var priority: Int = 0
        private var weight: Int = 1
        private var period: Int = 1

        fun executor(e: BehaviorExecutor) = apply { executor = e }
        fun evaluator(e: BehaviorEvaluator) = apply { evaluator = e }
        fun priority(p: Int) = apply { priority = p }
        fun weight(w: Int) = apply { weight = w }
        fun period(p: Int) = apply { period = p }
        fun build() = BehaviorImpl(executor, evaluator, priority, weight, period)
    }
}