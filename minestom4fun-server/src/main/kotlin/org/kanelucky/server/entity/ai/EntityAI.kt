package org.kanelucky.server.entity.ai

/**
 * Entity AI debug options
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
object EntityAI {
    enum class DebugOption { BEHAVIOR, SENSOR, CONTROLLER, ROUTE }

    private val debugOptions = mutableSetOf<DebugOption>()

    fun isDebugEnabled(option: DebugOption) = option in debugOptions
    fun enableDebug(option: DebugOption) { debugOptions.add(option) }
    fun disableDebug(option: DebugOption) { debugOptions.remove(option) }
}