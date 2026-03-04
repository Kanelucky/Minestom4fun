package org.kanelucky.server.entity.ai.memory

import net.minestom.server.coordinate.Point
import net.minestom.server.entity.LivingEntity
import net.minestom.server.entity.Player
import org.kanelucky.server.entity.passive.AnimalEntity

/**
 * Core memory types used by the AI framework
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
object MemoryTypes {
    val MOVE_TARGET = MemoryType<Point>("move_target")
    val LOOK_TARGET = MemoryType<Point>("look_target")
    val ATTACK_TARGET = MemoryType<LivingEntity>("attack_target")
    val NEAREST_PLAYER = MemoryType<Player>("nearest_player")
    val NEAREST_FEEDING_PLAYER = MemoryType<Player>("nearest_feeding_player")
    val IS_IN_LOVE = MemoryType<Boolean>("is_in_love")
    val LAST_IN_LOVE_TIME = MemoryType<Long>("last_in_love_time")
    val ENTITY_SPOUSE = MemoryType<AnimalEntity>("entity_spouse")
    val PANIC_TICKS = MemoryType<Int>("panic_ticks")
}