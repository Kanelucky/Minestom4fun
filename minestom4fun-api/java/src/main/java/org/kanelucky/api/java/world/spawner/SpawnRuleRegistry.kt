package org.kanelucky.api.java.world.spawner

import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author Kanelucky
 */
object SpawnRuleRegistry {
    private val rules: MutableList<SpawnRule?> = CopyOnWriteArrayList<SpawnRule?>()

    fun register(rule: SpawnRule?) {
        rules.add(rule)
    }

    val all: MutableList<SpawnRule?>
        get() = Collections.unmodifiableList<SpawnRule?>(rules)
}