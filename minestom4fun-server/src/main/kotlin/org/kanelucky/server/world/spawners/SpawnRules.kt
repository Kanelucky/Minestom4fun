package org.kanelucky.server.world.spawners

/**
 * Registry of all natural spawn rules
 *
 * @author Kanelucky
 */
object SpawnRules {
    val ALL: List<SpawnRule> = listOf(
        SheepSpawnRule,
        CowSpawnRule,
        PigSpawnRule,
        ChickenSpawnRule
    )
}