package org.kanelucky.server.world.spawner

import org.kanelucky.api.java.world.spawner.SpawnRuleRegistry

/**
 * @author Kanelucky
 */
object DefaultSpawnRules {
    fun initialize() {
        SpawnRuleRegistry.register(SheepSpawnRule)
        SpawnRuleRegistry.register(CowSpawnRule)
        SpawnRuleRegistry.register(PigSpawnRule)
        SpawnRuleRegistry.register(ChickenSpawnRule)
    }
}