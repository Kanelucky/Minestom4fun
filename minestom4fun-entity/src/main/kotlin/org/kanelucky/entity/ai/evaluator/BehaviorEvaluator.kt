package org.kanelucky.entity.ai.evaluator

import net.minestom.server.entity.EntityCreature

/**
 * Evaluates whether a behavior should be activated.
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
fun interface BehaviorEvaluator {
    fun evaluate(entity: EntityCreature): Boolean
}