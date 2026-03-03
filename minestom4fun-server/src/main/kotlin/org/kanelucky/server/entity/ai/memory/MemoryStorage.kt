package org.kanelucky.server.entity.ai.memory

/**
 * Storage for entity AI memory data.
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
interface MemoryStorage {
    fun <T> set(type: MemoryType<T>, value: T?)
    fun <T> get(type: MemoryType<T>): T?
    fun <T> clear(type: MemoryType<T>)
    fun clear()
    fun <T> isEmpty(type: MemoryType<T>): Boolean
    fun isEmpty(): Boolean
    fun <T> putIfAbsent(type: MemoryType<T>, value: T): Boolean
}