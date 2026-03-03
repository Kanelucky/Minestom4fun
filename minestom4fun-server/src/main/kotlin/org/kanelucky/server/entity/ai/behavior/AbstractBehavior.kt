package org.kanelucky.server.entity.ai.behavior

/**
 * Base class for behaviors that provides default state management
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
abstract class AbstractBehavior : Behavior {
    override var behaviorState: BehaviorState = BehaviorState.STOP
}