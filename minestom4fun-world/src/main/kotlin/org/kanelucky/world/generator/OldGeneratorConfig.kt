package org.kanelucky.world.generator

import org.kanelucky.config.ConfigManager

/**
 * @author Kanelucky
 */
data class OldGeneratorConfig(
    val seed: Long = ConfigManager.worldSettings.seed,
    val seaLevel: Int = 64,
    val largeBiomes: Boolean = false
)